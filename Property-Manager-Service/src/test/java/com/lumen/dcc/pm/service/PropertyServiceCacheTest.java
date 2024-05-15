package com.lumen.dcc.pm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.EnableCaching;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;
import com.lumen.dcc.pm.repository.PropertyRepository;
import com.lumen.dcc.pm.transformer.PropertyTransformer;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableCaching
class PropertyServiceCacheTest {
	
	@Autowired
    private PropertyService propertyService;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private PropertyTransformer propertyTransformer;
    
    @Autowired
    private CacheManager cacheManager;
    
    
    ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
    Application appEntity = new Application(1L,"shalini","it is name");
    
    PropertyTypeDTO pty=new PropertyTypeDTO(1L,"c1","n1");
    PropertyType ptyEnt=new PropertyType(1L,"c1","n1");

    @Test
    void testCreateProperty_CacheHits() {
    	
    	PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        when(propertyRepository.findById(1L)).thenReturn(null);
        when(propertyTransformer.transformToEntity(propertyDTO)).thenReturn(property);
        when(propertyRepository.save(property)).thenReturn(property);
        when(propertyTransformer.transformToDto(property)).thenReturn(propertyDTO);
        PropertyDTO result = propertyService.createProperty(propertyDTO);
        assertNotNull(result);
        verify(propertyRepository, times(1)).save(property);
        Cache applicationCache = cacheManager.getCache("property");
        assertNotNull(applicationCache);
    }
    @Test
    void testCreateProperty_CacheFailure() {
    	
    	PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        when(propertyRepository.existsById(1L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> propertyService.createProperty(propertyDTO));
        verify(propertyRepository, never()).save(property);
        ValueWrapper applicationCache = cacheManager.getCache("property").get(2);
        assertNull(applicationCache); 
    }
    
    
    @Test
    void testGetPropertyByID_CacheHits() {
    	PropertyDTO expectedPropertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        long id=1L;
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.getById(id)).thenReturn(property);
        when(propertyTransformer.transformToDto(property)).thenReturn(expectedPropertyDTO);
        PropertyDTO retrievedDto = propertyService.getPropertyByID(id); 
        ValueWrapper applicationCache = cacheManager.getCache("property").get(id);
        assertNotNull(applicationCache);
    }
   
    @Test
    void testGetPropertyByID_CacheFailure() {

        Long propertyId = 1L;
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
    	when(propertyRepository.findById(1L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyService.getPropertyByID(propertyId));
        verify(propertyTransformer, never()).transformToDto(property); 
        ValueWrapper applicationCache = cacheManager.getCache("property").get(propertyId);
        assertNull(applicationCache); 
       
    }
    
    @Test
    void testGetAll_CacheHits() {
       
        List<Property> applications = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        applications.add(p1);
        applications.add(p2);
        List<PropertyDTO> expectedDTOs = new ArrayList<>();
        PropertyDTO dto1 = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        PropertyDTO dto2 = new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1");
        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);
        when(propertyRepository.findAll()).thenReturn(applications);
        when(propertyTransformer.transformToDto(applications)).thenReturn(expectedDTOs);
        List<PropertyDTO> result = propertyService.getAll();
        assertEquals(expectedDTOs.size(), result.size());
        Cache applicationCache = cacheManager.getCache("properties");
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetAll_CacheFailure() {
     
        when(propertyRepository.findAll()).thenReturn(Collections.emptyList());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyService.getAll());
        assertEquals("Property not exist", exception.getMessage());
        verify(propertyRepository, times(1)).findAll();
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }
    @Test
    void testUpdateProperty_CacheHits() {
     
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
   
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(propertyRepository.save(property)).thenReturn(property);
        when(propertyTransformer.transformToEntity(propertyDTO)).thenReturn(property);
        when(propertyTransformer.transformToDto(property)).thenReturn(propertyDTO);
        PropertyDTO result = propertyService.UpdateProperty(propertyId, propertyDTO);
        assertNotNull(result);
        verify(propertyRepository, times(1)).findById(propertyId);
        verify(propertyRepository, times(1)).save(property);
        ValueWrapper applicationCache = cacheManager.getCache("property").get(propertyId);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testUpdateProperty_CacheFailure() {
     
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyRepository.findById(1L)).thenReturn(null); 
        assertThrows(RuntimeException.class, () -> propertyService.UpdateProperty(propertyId, propertyDTO));
        verify(propertyRepository, times(1)).findById(propertyId);
        verify(propertyRepository, never()).save(property);
        Cache applicationCache = cacheManager.getCache("property");
        assertNull(applicationCache); 
    }
    
    @Test
    void testRemovePropertyByID_CacheHits() {
     
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        property.setPropertyId(propertyId);
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        propertyService.removePropertyByID(propertyId);
        verify(propertyRepository, times(1)).findById(propertyId);
        verify(propertyRepository, times(1)).deleteById(propertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(propertyId);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testRemovePropertyByID_CacheFailure() {
       
        Long propertyId = 1L;
        when(propertyRepository.findById(1L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyService.removePropertyByID(propertyId));
        verify(propertyRepository, times(1)).findById(propertyId);
        verify(propertyRepository, never()).deleteById(propertyId);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(propertyId);
        assertNull(applicationCache); 
    }
    
    @Test
    void testGetByPropertyNameAndEngineAndEnvironment_CacheHits() {
     
    	String propertyName = "n1";
        String engine = "e1";
        String environment = "dev";
        List<Property> property = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        property.add(p1);
        property.add(p2);
        List<PropertyDTO> expectedPropertyDTO = new ArrayList<>();
        PropertyDTO dto1 = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        PropertyDTO dto2 = new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1");
        expectedPropertyDTO.add(dto1);
        expectedPropertyDTO.add(dto2);
        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(property);
        when(propertyTransformer.transformToDto(property)).thenReturn(expectedPropertyDTO);
        List<PropertyDTO> result = propertyService.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        assertNotNull(result);
        verify(propertyRepository, times(2)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(environment);
        assertNotNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache1);
        ValueWrapper applicationCache2 = cacheManager.getCache("properties").get(propertyName);
        assertNotNull(applicationCache2);
       
    }
    @Test
    void testGetByPropertyNameAndEngineAndEnvironment_CacheFailure() {
       
        String propertyName = "Property Name";
        String engine = "Engine";
        String environment = "Environment";
        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyService.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
        verify(propertyRepository, times(0)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(environment);
        assertNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache1);
        ValueWrapper applicationCache2 = cacheManager.getCache("properties").get(propertyName);
        assertNull(applicationCache2);
    }
    
    @Test
    void testGetByEngineAndEnvironment_CacheHits() {
    	
       
        String engine = "e1";
        String environment = "dev";
        List<Property> property = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        property.add(p1);
        property.add(p2);
        List<PropertyDTO> expectedPropertyDTO = new ArrayList<>();
        PropertyDTO dto1 = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        PropertyDTO dto2 = new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1");
        expectedPropertyDTO.add(dto1);
        expectedPropertyDTO.add(dto2);
        when(propertyRepository.getByEngineAndEnvironment(engine, environment)).thenReturn(property);
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(expectedPropertyDTO);
        List<PropertyDTO> result = propertyService.getByEngineAndEnvironment(engine, environment);
        assertNotNull(result);
        verify(propertyRepository, times(4)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(environment);
        assertNotNull(applicationCache); 
    }
    @Test
    void testGetByEngineAndEnvironment_CacheFailure() {
   
        String engine = "Engine";
        String environment = "Environment";
        when(propertyRepository.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyService.getByEngineAndEnvironment(engine, environment));
        verify(propertyRepository, times(1)).getByEngineAndEnvironment(engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("properties").get(environment);
        assertNull(applicationCache); 
    }
    @Test
    void testGetByEngine_CacheHits() {
    	String engine="e1";
        List<Property> applications = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        applications.add(p1);
        applications.add(p2);
        List<PropertyDTO> expectedDTOs = new ArrayList<>();
        PropertyDTO dto1 = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        PropertyDTO dto2 = new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1");
        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);
        when(propertyRepository.getByEngine(engine)).thenReturn(applications);
        when(propertyTransformer.transformToDto(applications)).thenReturn(expectedDTOs);
        List<PropertyDTO> result = propertyService.getByEngine(engine);
        assertEquals(expectedDTOs.size(), result.size());
        verify(propertyRepository, times(2)).getByEngine(engine);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetByEngine_CacheFailure() {
        String engine = "e2";
        when(propertyRepository.getByEngine(engine)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> propertyService.getByEngine(engine));
        verify(propertyRepository, times(1)).getByEngine(engine);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(engine);
        assertNull(applicationCache); 
      
    }
    
    @Test
    void testGetByEnvironment_CacheHits() {
    	String envi="dev";
        List<Property> applications = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        applications.add(p1);
        applications.add(p2);
        List<PropertyDTO> expectedDTOs = new ArrayList<>();
        PropertyDTO dto1 = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        PropertyDTO dto2 = new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1");
        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);
        when(propertyRepository.getByEnvironment(envi)).thenReturn(applications);
        when(propertyTransformer.transformToDto(applications)).thenReturn(expectedDTOs);
        List<PropertyDTO> result = propertyService.getByEnvironment(envi);
        assertEquals(expectedDTOs.size(), result.size());
        verify(propertyRepository, times(2)).getByEnvironment(envi);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(envi);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetByEnvironment_CacheFailure() {
     
        String envi = "devsss";
        when(propertyRepository.getByEnvironment(envi)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> propertyService.getByEnvironment(envi));
        verify(propertyRepository, times(1)).getByEnvironment(envi);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(envi);
        assertNull(applicationCache); 
    }
    
    @Test
    void testDeleteAllByPropertyNameAndEngineAndEnvironment_CacheHits() {
       
        String propertyName = "n1";
        String engine = "e1";
        String environment = "dev";
        List<Property> property = new ArrayList<>();
        Property p1 = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        Property p2 = new Property(2L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        property.add(p1);
        property.add(p2);
        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(property);
        propertyService.deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        verify(propertyRepository, times(1)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        verify(propertyRepository, times(1)).deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        Cache applicationCache = cacheManager.getCache("properties");
        assertNotNull(applicationCache);
    }
    
    @Test
    void testDeleteAllByPropertyNameAndEngineAndEnvironment_CacheFailure() {
        
        String propertyName = "Property Name";
        String engine = "Engine";
        String environment = "Environment";
        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyService.deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
        verify(propertyRepository, times(1)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        verify(propertyRepository, never()).deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
        ValueWrapper applicationCache = cacheManager.getCache("properties").get(1);
        assertNull(applicationCache); 
    }

	

}
