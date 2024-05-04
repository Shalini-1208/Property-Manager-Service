package com.lumen.dcc.pm.transformer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.entity.Application;

@SpringBootTest
class ApplicationTransformerTest {
	
	
	@Autowired
	private ApplicationTransformer transformer;


	
	@Test
	 void testTransformToDto_Success() {
	  
		
	    
	        Application appEntity = new Application();
	        appEntity.setApplicationId(1L);
	        appEntity.setName("shalini");
	        appEntity.setDescription("it is name");
	   

	     ApplicationDTO result = transformer.transformToDto(appEntity);
	     assertEquals(appEntity.getApplicationId(), result.getApplicationId());
	     assertEquals(appEntity.getName(), result.getName());
	     assertEquals(appEntity.getDescription(), result.getDescription());
	 }
		

	   @Test
	    void testListTransformToDto_Success() {
	   
	        List<Application> applications = new ArrayList<>();
	        Application app1 = new Application(1L,"shalini","it is name");
	        Application app2 = new Application(2L,"shalini","it is name");
	        applications.add(app1);
	        applications.add(app2);
	        List<ApplicationDTO> result = transformer.transformToDto(applications);
	        assertEquals(applications.get(1).getApplicationId(), result.get(1).getApplicationId());
         assertEquals(applications.get(1).getName(), result.get(1).getName());
         assertEquals(applications.get(1).getDescription(), result.get(1).getDescription());

	            
	        
    }

	   @Test
	    void testTransformToEntity_Success() {
	     
	        ApplicationDTO dto= new ApplicationDTO(1,"shalini","it is name");
	       
	        Application result = transformer.transformToEntity(dto);
	        assertEquals(dto.getApplicationId(), result.getApplicationId());
	        assertEquals(dto.getName(), result.getName());
	        assertEquals(dto.getDescription(), result.getDescription());
	    }

	   @Test
	    void testListTransformToEntity_Success() {
	   
	       
	        List<ApplicationDTO> dtos = new ArrayList<>();
	        ApplicationDTO dto1= new ApplicationDTO(1,"shalini","it is name");
	        ApplicationDTO dto2= new ApplicationDTO(2L,"shalini","it is name");
	        dtos.add(dto1);
	        dtos.add(dto2);
	        List<Application> result =transformer.transformToEntity(dtos);

	            assertEquals(dtos.get(1).getApplicationId(), result.get(1).getApplicationId());
	            assertEquals(dtos.get(1).getName(), result.get(1).getName());
	            assertEquals(dtos.get(1).getDescription(), result.get(1).getDescription());
	        }

}

