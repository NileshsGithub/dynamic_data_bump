package com.example.dynamic_data_bump.controller;


import com.example.dynamic_data_bump.service.PersonService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/load-mapped")
    public ResponseEntity<String> loadMappedFromClasspath() throws IOException, CsvValidationException {
        int count = personService.uploadUsingMappedFields(
                new ClassPathResource("csv/persons.csv").getInputStream()
        );
        return ResponseEntity.ok("Loaded " + count + " persons using mapped fields.");
    }
}
