package com.lumen.dcc.pm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumen.dcc.pm.entity.Property;

import jakarta.transaction.Transactional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> getByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment);
    List<Property> getByEngineAndEnvironment(String engine, String environment);
    @Transactional
    void deleteAllByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment);
    List<Property> getByEngine(String engine);
    List<Property> getByEnvironment(String environment);
   
}
