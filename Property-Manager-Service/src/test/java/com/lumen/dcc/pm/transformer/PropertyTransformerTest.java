package com.lumen.dcc.pm.transformer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;

@SpringBootTest
class PropertyTransformerTest {
	
	 	@Autowired
	    private ApplicationTransformer applicationTransformer;

	 	@Autowired
	    private PropertyTypeTransformer propertyTypeTransformer;

	 	@Autowired
	    private PropertyTransformer propertyTransformer;

	    @Test
	    void testTransformToDto_Success() {
	       
	        Property property = new Property();
	        property.setPropertyId(1L);
	        Application application = new Application();
	        application.setApplicationId(1L);
	        application.setName("shalini");
	        property.setApplication(application);
	        PropertyType propertyType = new PropertyType();
	        propertyType.setPropertyTypeId(1L);
	        propertyType.setName("n1");
	        property.setPropertyType(propertyType);
	        property.setEngine("e1");
	        property.setEnvironment("dev");
	        property.setPropertyName("n1");
	        property.setPropertyValue("v1");
	        PropertyDTO result = propertyTransformer.transformToDto(property);

	        assertEquals(property.getPropertyId(), result.getPropertyId());
	        assertNotNull(result.getApplication());
	        assertEquals(property.getApplication().getApplicationId(), result.getApplication().getApplicationId());
	        assertNotNull(result.getPropertyType());
	        assertEquals(property.getPropertyType().getPropertyTypeId(), result.getPropertyType().getPropertyTypeId());
	        assertEquals(property.getEngine(), result.getEngine());
	        assertEquals(property.getEnvironment(), result.getEnvironment());
	        assertEquals(property.getPropertyName(), result.getPropertyName());
	        assertEquals(property.getPropertyValue(), result.getPropertyValue());
	    }
	    
	    @Test
	    void testTransformToEntity_Success() {
	     
	        PropertyDTO propertyDTO = new PropertyDTO();
	        propertyDTO.setPropertyId(1L);
	        ApplicationDTO applicationDTO = new ApplicationDTO();
	        applicationDTO.setApplicationId(1L);
	        applicationDTO.setName("Test Application");
	        propertyDTO.setApplication(applicationDTO);
	        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO();
	        propertyTypeDTO.setPropertyTypeId(1L);
	        propertyTypeDTO.setName("Test Property Type");
	        propertyDTO.setPropertyTypeId(propertyTypeDTO);
	        propertyDTO.setEngine("Test Engine");
	        propertyDTO.setEnvironment("Test Environment");
	        propertyDTO.setPropertyName("Test Property");
	        propertyDTO.setPropertyValue("Test Value");
	        Property result = propertyTransformer.transformToEntity(propertyDTO);

	        assertEquals(propertyDTO.getPropertyId(), result.getPropertyId());
	        assertNotNull(result.getApplication());
	        assertEquals(propertyDTO.getApplication().getApplicationId(), result.getApplication().getApplicationId());
	        assertNotNull(result.getPropertyType());
	        assertEquals(propertyDTO.getPropertyType().getPropertyTypeId(), result.getPropertyType().getPropertyTypeId());
	        assertEquals(propertyDTO.getEngine(), result.getEngine());
	        assertEquals(propertyDTO.getEnvironment(), result.getEnvironment());
	        assertEquals(propertyDTO.getPropertyName(), result.getPropertyName());
	        assertEquals(propertyDTO.getPropertyValue(), result.getPropertyValue());
	    }
	    
	    
	    @Test
	    void testListTransformToDto_Success() {
	    
	        List<Property> properties = new ArrayList<>();
	        Property property1 = new Property();
	        property1.setPropertyId(1L);
	        Application application1 = new Application();
	        application1.setApplicationId(1L);
	        application1.setName("Application 1");
	        property1.setApplication(application1);
	        PropertyType propertyType1 = new PropertyType();
	        propertyType1.setPropertyTypeId(1L);
	        propertyType1.setName("Property Type 1");
	        property1.setPropertyType(propertyType1);
	        property1.setEngine("Engine 1");
	        property1.setEnvironment("Environment 1");
	        property1.setPropertyName("Property Name 1");
	        property1.setPropertyValue("Property Value 1");
	        properties.add(property1);

	        List<PropertyDTO> result = propertyTransformer.transformToDto(properties);
	        int i=0;
	        Property property = properties.get(i);
	        PropertyDTO propertyDTO = result.get(i);
	        assertEquals(property.getPropertyId(), propertyDTO.getPropertyId());
	        assertEquals(property.getApplication().getApplicationId(), propertyDTO.getApplication().getApplicationId());
	        assertEquals(property.getPropertyType().getPropertyTypeId(), propertyDTO.getPropertyType().getPropertyTypeId());
	        assertEquals(property.getEngine(), propertyDTO.getEngine());
	        assertEquals(property.getEnvironment(), propertyDTO.getEnvironment());
	        assertEquals(property.getPropertyName(), propertyDTO.getPropertyName());
	        assertEquals(property.getPropertyValue(), propertyDTO.getPropertyValue());
	        
	    }
	    
	    
	    @Test
	    void testListTransformToEntity_Success() {
	      
	        List<PropertyDTO> propertyDTOs = new ArrayList<>();
	        PropertyDTO propertyDTO1 = new PropertyDTO();
	        propertyDTO1.setPropertyId(1L);
	        ApplicationDTO applicationDTO1 = new ApplicationDTO();
	        applicationDTO1.setApplicationId(1L);
	        applicationDTO1.setName("Application 1");
	        propertyDTO1.setApplication(applicationDTO1);
	        PropertyTypeDTO propertyTypeDTO1 = new PropertyTypeDTO();
	        propertyTypeDTO1.setPropertyTypeId(1L);
	        propertyTypeDTO1.setName("Property Type 1");
	        propertyDTO1.setPropertyTypeId(propertyTypeDTO1);
	        propertyDTO1.setEngine("Engine 1");
	        propertyDTO1.setEnvironment("Environment 1");
	        propertyDTO1.setPropertyName("Property Name 1");
	        propertyDTO1.setPropertyValue("Property Value 1");
	        propertyDTOs.add(propertyDTO1);
	       
	        List<Property> result = propertyTransformer.transformToEntity(propertyDTOs);
	        int i=0;
	        PropertyDTO propertyDTO = propertyDTOs.get(i);
	        Property property = result.get(i);
	        assertEquals(propertyDTO.getPropertyId(), property.getPropertyId());
	        assertEquals(propertyDTO.getApplication().getApplicationId(), property.getApplication().getApplicationId());
	        assertEquals(propertyDTO.getPropertyType().getPropertyTypeId(), property.getPropertyType().getPropertyTypeId());
	        assertEquals(propertyDTO.getEngine(), property.getEngine());
	        assertEquals(propertyDTO.getEnvironment(), property.getEnvironment());
	        assertEquals(propertyDTO.getPropertyName(), property.getPropertyName());
	        assertEquals(propertyDTO.getPropertyValue(), property.getPropertyValue());
	        
	    }

	

}

