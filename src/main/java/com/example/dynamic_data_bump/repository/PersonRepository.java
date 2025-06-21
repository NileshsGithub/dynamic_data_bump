package com.example.dynamic_data_bump.repository;

import com.example.dynamic_data_bump.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
