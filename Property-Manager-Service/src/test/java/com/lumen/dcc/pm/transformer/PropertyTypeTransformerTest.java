package com.lumen.dcc.pm.transformer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.PropertyType;

@SpringBootTest
class PropertyTypeTransformerTest {
	
	@Autowired
	private PropertyTypeTransformer transformer;
	
	@Test
	 void testTransformToDto_Success() {
	  
	     PropertyType propertyType = new PropertyType(1L,"c1","n1");
	     PropertyTypeDTO result = transformer.transformToDto(propertyType);
	     assertEquals(propertyType.getPropertyTypeId(), result.getPropertyTypeId());
	     assertEquals(propertyType.getName(), result.getName());
	     assertEquals(propertyType.getImplementationClass(), result.getImplementationClass());
	 }
		

	   @Test
	    void testListTransformToDto_Success() {
	   
	     List<PropertyType> applications = new ArrayList<>();
	     PropertyType app1 = new PropertyType(1L,"n1","c1");
	     PropertyType app2 = new PropertyType(2L,"n1","c1");
	     applications.add(app1);
	     applications.add(app2);
	     List<PropertyTypeDTO> result = transformer.transformToDto(applications);
	     assertEquals(applications.get(1).getPropertyTypeId(), result.get(1).getPropertyTypeId());
         assertEquals(applications.get(1).getName(), result.get(1).getName());
         assertEquals(applications.get(1).getImplementationClass(), result.get(1).getImplementationClass());	        
    }

	   @Test
	    void testTransformToEntity_Success() {
	     
		   PropertyTypeDTO propertyType = new PropertyTypeDTO(1L,"c1","n1");
		   PropertyType result = transformer.transformToEntity(propertyType);
		   assertEquals(propertyType.getPropertyTypeId(), result.getPropertyTypeId());
		   assertEquals(propertyType.getName(), result.getName());
		   assertEquals(propertyType.getImplementationClass(), result.getImplementationClass());
	    }

	   @Test
	    void testListTransformToEntity_Success() {
		   List<PropertyTypeDTO> applications = new ArrayList<>();
		   PropertyTypeDTO app1 = new PropertyTypeDTO(1L,"n1","c1");
		   PropertyTypeDTO app2 = new PropertyTypeDTO(2L,"n1","c1");
		   applications.add(app1);
		   applications.add(app2);
		   List<PropertyType> result = transformer.transformToEntity(applications);
		   assertEquals(applications.get(1).getPropertyTypeId(), result.get(1).getPropertyTypeId());
	       assertEquals(applications.get(1).getName(), result.get(1).getName());
	       assertEquals(applications.get(1).getImplementationClass(), result.get(1).getImplementationClass());
	   }
	   
	   
	       
	        

}

