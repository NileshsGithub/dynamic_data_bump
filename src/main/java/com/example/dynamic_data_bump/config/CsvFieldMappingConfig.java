package com.example.dynamic_data_bump.config;

import com.example.dynamic_data_bump.config.FieldMapping;
import lombok.Data;
import java.util.List;

@Data
public class CsvFieldMappingConfig {
    private List<FieldMapping> mappings;
}
