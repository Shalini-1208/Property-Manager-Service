package com.lumen.dcc.pm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;
import com.lumen.dcc.pm.service.ApplicationService;
import com.lumen.dcc.pm.service.PropertyService;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableCaching
class PropertyControllerCacheTest {
	
	@InjectMocks
    private PropertyController propertyController;

    @Mock
    private PropertyService propertyService;
   
    @Autowired
    private CacheManager cacheManager;
     
    
    ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
    Application appEntity = new Application(1L,"shalini","it is name");
    
    PropertyTypeDTO pty=new PropertyTypeDTO(1L,"c1","n1");
    PropertyType ptyEnt=new PropertyType(1L,"c1","n1");

    @Test
    void testCreateProperty_CacheHits() {
    
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.createProperty(propertyDTO)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.createProperty(propertyDTO);
        assertNotNull(result);
        verify(propertyService, times(1)).createProperty(propertyDTO);
        Cache applicationCache = cacheManager.getCache("properties");
        assertNotNull(applicationCache);
    }
    
    @Test
    void testCreateProperty_CacheFailure() {
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property already exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.createProperty(propertyDTO));
        assertEquals("Property already exist", exception.getMessage());
        verify(propertyService, times(1)).createProperty(propertyDTO);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }
    @Test
    void testUpdateProperty_CacheHits() {
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.UpdateProperty(propertyId, propertyDTO)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.updateProperty(propertyId, propertyDTO);
        assertNotNull(result);
        verify(propertyService, times(1)).UpdateProperty(propertyId, propertyDTO);
        Cache applicationCache = cacheManager.getCache("properties");
        assertNotNull(applicationCache);
    }
    @Test
    void testUpdateProperty_CacheFailure() {
      
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        propertyDTO.setPropertyName("Updated Property");
        when(propertyService.UpdateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Update failed"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.updateProperty(propertyId, propertyDTO));
        assertEquals("Update failed", exception.getMessage());
        verify(propertyService, times(1)).UpdateProperty(propertyId, propertyDTO);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }
    @Test
    void testGetPropertyById_CacheHits() {
     
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.getPropertyByID(propertyId)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.getPropertyById(propertyId);
        assertNotNull(result);
        verify(propertyService, times(1)).getPropertyByID(propertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(propertyId);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetPropertyById_CacheFailure() {
 
        Long invalidPropertyId = -1L;
        when(propertyService.getPropertyByID(invalidPropertyId)).thenThrow(new RuntimeException("Property id not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyById(invalidPropertyId));
        assertEquals("Property id not exist", exception.getMessage());
        verify(propertyService, times(1)).getPropertyByID(invalidPropertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(-1);
        assertNull(applicationCache); 
    }
    
    @Test
    void testGetAllProperties_CacheHits() {

        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getAll()).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getAllProperties();
        assertNotNull(result);
        verify(propertyService, times(1)).getAll();
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNotNull(applicationCache);
    }
    @Test
    void testGetAllProperties_CacheFailure() {
       
        when(propertyService.getAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<PropertyDTO>>result = propertyController.getAllProperties();
        assertNotNull(result);
        assertTrue(result.getBody().isEmpty());
        verify(propertyService, times(1)).getAll();
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }
    
    @Test
    void testDeletePropertyById_CacheHits() {
  
        Long propertyId = 1L;
        propertyController.deletePropertyById(propertyId);
        verify(propertyService, times(1)).removePropertyByID(propertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(propertyId);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testDeletePropertyById_CacheFailure() {
      
        Long invalidPropertyId = -1L;
        doThrow(new RuntimeException("Property not found")).when(propertyService).removePropertyByID(invalidPropertyId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.deletePropertyById(invalidPropertyId));
        assertEquals("Property not found", exception.getMessage());
        verify(propertyService, times(1)).removePropertyByID(invalidPropertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }
    
    @Test
    void testGetPropertyByEngine_CacheHits() {
      
        String engine = "e1";
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEngine(engine)).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEngine(engine);
        assertNotNull(result);
        verify(propertyService, times(1)).getByEngine(engine);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetPropertyByEngine_CacheFailure() {
        String engine = "TestEngine";
        when(propertyService.getByEngine(engine)).thenThrow(new RuntimeException("Property not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyByEngine(engine));
        assertEquals("Property not exist", exception.getMessage());
        verify(propertyService, times(1)).getByEngine(engine);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache); 
    }
    @Test
    void testGetPropertyByEnvironment_CacheHits() {
      
        String envi = "dev";
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEnvironment(envi)).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEnvironment(envi);
        assertNotNull(result);
        verify(propertyService, times(1)).getByEnvironment(envi);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(envi);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetPropertyByEnvironment_CacheFailure() {
        String envi = "TestEngine";
        when(propertyService.getByEnvironment(envi)).thenThrow(new RuntimeException("Property not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyByEnvironment(envi));
        assertEquals("Property not exist", exception.getMessage());
        verify(propertyService, times(1)).getByEnvironment(envi);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(envi);
        assertNull(applicationCache); 
    }
    
    @Test
    void testGetPropertyByEngineAndEnvironment_Success() {
        
        String engine = "e1";
        String environment = "dev";
        List<PropertyDTO> expectedPropertyDTO = new ArrayList<>();
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(expectedPropertyDTO);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEngineAndEnvironment(engine, environment);
        assertNotNull(result);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache); 
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(environment);
        assertNotNull(applicationCache1); 
    }
    @Test
    void testGetPropertyByEngineAndEnvironment_CacheFailure() {
       
        String engine = "TestEngine";
        String environment = "TestEnvironment";
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEngineAndEnvironment(engine, environment);
        assertNotNull(result);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache); 
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(environment);
        assertNull(applicationCache1); 
    }
    
    @Test
    void testGetPropertyByNameAndEngineAndEnvironment_CacheHits() {
    
        String name = "n1";
        String engine = "e1";
        String environment = "devt";
       
        List<PropertyDTO> expectedPropertyDTO = new ArrayList<>();
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(expectedPropertyDTO);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByNameAndEngineAndEnvironment(name, engine, environment);
        assertNotNull(result);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(name);
        assertNotNull(applicationCache); 
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache1); 
        ValueWrapper applicationCache2 = cacheManager.getCache("properties").get(environment);
        assertNotNull(applicationCache2); 
    }
    
    @Test
    void testGetPropertyByNameAndEngineAndEnvironment_CacheFailure() {
        String name = "TestName";
        String engine = "TestEngine";
        String environment = "TestEnvironment";
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByNameAndEngineAndEnvironment(name, engine, environment);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(name);
        assertNull(applicationCache); 
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache1); 
        ValueWrapper applicationCache2 = cacheManager.getCache("properties").get(environment);
        assertNull(applicationCache2); 
    }
    
    @Test
    void testDeltePropertyByNameAndEngineAndEnvironment_CacheHits() {
      
        String name = "n1";
        String engine = "e1";
        String environment = "dev";
        propertyController.deletePropertyByNameAndEngineAndEnvironment(name, engine, environment);
        verify(propertyService, times(1)).deleteAllByPropertyNameAndEngineAndEnvironment(name, engine, environment);
        Cache applicationCache = cacheManager.getCache("properties");
        assertNotNull(applicationCache);
    }
    
}

  
    
    
	


