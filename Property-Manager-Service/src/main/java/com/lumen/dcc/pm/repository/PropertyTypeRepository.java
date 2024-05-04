package com.lumen.dcc.pm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumen.dcc.pm.entity.PropertyType;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {
	   
    List<PropertyType> getByName(String name);
    PropertyType getByPropertyTypeIdAndName(Long propertyTypeId,String name);
    PropertyType getById(Long id);
      
}