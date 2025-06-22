package com.example.dynamic_data_bump.service;

import com.example.dynamic_data_bump.config.CsvFieldMappingConfig;
import com.example.dynamic_data_bump.config.FieldMapping;
import com.example.dynamic_data_bump.entity.Person;
import com.example.dynamic_data_bump.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Integer uploadUsingMappedFields(InputStream inputStream) throws IOException, CsvValidationException {
        CsvFieldMappingConfig config = loadFieldMappingConfig();

        Map<String, String> fieldMap = config.getMappings().stream()
                .collect(Collectors.toMap(
                        map -> map.getSourceField().toLowerCase(),
                        FieldMapping::getDestinationField
                ));

        List<Person> people = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            if (headers == null) return 0;

            Map<String, Integer> headerIndexMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerIndexMap.put(headers[i].trim().toLowerCase(), i);
            }

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Person person = new Person();

                for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                    String source = entry.getKey();
                    String destination = entry.getValue();
                    int index = headerIndexMap.getOrDefault(source, -1);
                    if (index == -1 || index >= row.length) continue;

                    String value = row[index];

                    switch (destination) {
                        case "firstName" -> person.setFirstName(value);
                        case "lastName" -> person.setLastName(value);
                        case "mobileNo" -> person.setMobileNo(value);
                    }
                }
                people.add(person);
            }
        }
        personRepository.saveAll(people);
        return people.size();
    }

    private CsvFieldMappingConfig loadFieldMappingConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                new ClassPathResource("config/config.json").getInputStream(),
                CsvFieldMappingConfig.class
        );
    }
}
