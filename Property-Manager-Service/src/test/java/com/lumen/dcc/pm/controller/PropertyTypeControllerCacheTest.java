
package com.lumen.dcc.pm.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.service.PropertyTypeService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableCaching
class PropertyTypeControllerCacheTest {
	@Autowired
    private CacheManager cacheManager;
	
	@Mock
    private PropertyTypeService service;

    @InjectMocks
    private PropertyTypeController controller;

    @Test
    public void testCreatePropertyType_CacheHits() {
      
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.createPropertyType(propertyTypeDTO)).thenReturn(propertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.createPropertyType(propertyTypeDTO);
        verify(service, times(1)).createPropertyType(propertyTypeDTO);
        assertNotNull(result);
        Cache applicationCache = cacheManager.getCache("propertyTypes");
        assertNotNull(applicationCache);
    }
    
    @Test
    void testCreatePropertyType_CacheFailure() {
    
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.createPropertyType(propertyTypeDTO)).thenThrow(new RuntimeException("PropertyType already exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.createPropertyType(propertyTypeDTO));
        assertEquals("PropertyType already exist", exception.getMessage());
        verify(service, times(1)).createPropertyType(propertyTypeDTO);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(1);
        assertNull(applicationCache); 
        
    }
    
    @Test
    public void testUpdatePropertyType_CacheHits() {
        Long propertyTypeId = 1L;
        PropertyTypeDTO samplePropertyType = new PropertyTypeDTO(1L,"c1","n1");
        when(service.UpdatePropertyType(propertyTypeId, samplePropertyType)).thenReturn(samplePropertyType);
        ResponseEntity<PropertyTypeDTO> result = controller.updatePropertyType(propertyTypeId, samplePropertyType);
        verify(service, times(1)).UpdatePropertyType(propertyTypeId, samplePropertyType);
        assertNotNull(result);
        verify(service, times(1)).UpdatePropertyType(propertyTypeId, samplePropertyType);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(propertyTypeId);
        assertNotNull(applicationCache);
        
    }
    
    @Test
    public void testUpdatePropertyType_CacheFailure() {
        Long invalidId = -1L;
        PropertyTypeDTO samplePropertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.UpdatePropertyType(invalidId, samplePropertyTypeDTO)).thenThrow(new RuntimeException("PropertyType not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.updatePropertyType(invalidId, samplePropertyTypeDTO));
        assertEquals("PropertyType not exist", exception.getMessage());
        verify(service, times(1)).UpdatePropertyType(invalidId, samplePropertyTypeDTO);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(1);
        assertNull(applicationCache); 

    }
    @Test
    void testGetPropertyTypeById_CacheHits() {
        long id = 1L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        when(service.getPropertyTypeByID(id)).thenReturn(propertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.getPropertyTypeById(id);
        verify(service, times(1)).getPropertyTypeByID(id);
        assertEquals(propertyTypeDTO, result.getBody());
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(id);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetPropertyTypeById_CacheFailure() {
        long id = 1L;
        when(service.getPropertyTypeByID(id)).thenThrow(new RuntimeException("PropertyType not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getPropertyTypeById(id));
        assertEquals("PropertyType not exist", exception.getMessage());
        verify(service, times(1)).getPropertyTypeByID(id);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(id);
        assertNull(applicationCache); 
    }
    @Test
    void testDeletePropertyTypeById_CacheHits() {
        long id = 1L;
        controller.deletePropertyById(id);
        verify(service, times(1)).removePropertyTypeByID(id);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(id);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testDeletePropertyTypeById_CacheFailure() {
        long id = 1L;
        doThrow(new RuntimeException("PropertyType not exist")).when(service).removePropertyTypeByID(id);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.deletePropertyById(id));
        assert(exception.getMessage().equals("PropertyType not exist"));
        verify(service, times(1)).removePropertyTypeByID(id);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(id);
        assertNull(applicationCache); 
    }
    @Test
    public void testGetByPropertyTypeIdAndName_CacheHits() {
       
        Long propertyTypeId = 1L;
        String propertyName = "sampleProperty";
        PropertyTypeDTO samplePropertyTypeDTO = new PropertyTypeDTO(1L, "c1","n1");
        when(service.getByPropertyTypeIdAndName(propertyTypeId, propertyName)).thenReturn(samplePropertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.getByPropertyTypeIdAndName(propertyTypeId, propertyName);
        verify(service, times(1)).getByPropertyTypeIdAndName(propertyTypeId, propertyName);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(propertyTypeId);
        assertNotNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("propertyTypes").get(propertyName);
        assertNotNull(applicationCache1);
        assertNotNull(result);
    
    }
    @Test
    void testGetByPropertyTypeIdAndName_CacheFailure() {
        
        long id = 1L;
        String name = "TestName";
        doThrow(new RuntimeException("PropertyType not exist")).when(service).getByPropertyTypeIdAndName(id, name);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getByPropertyTypeIdAndName(id, name));
        assert(exception.getMessage().equals("PropertyType not exist"));
        verify(service, times(1)).getByPropertyTypeIdAndName(id, name);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(id);
        assertNull(applicationCache);
        ValueWrapper applicationCache1 = cacheManager.getCache("propertyTypes").get(name);
        assertNull(applicationCache1);
    }
    
    @Test
    public void testGetAllPropertyTypes_CacheHits() {
        List<PropertyTypeDTO> samplePropertyTypes = new ArrayList<>();
        samplePropertyTypes.add(new PropertyTypeDTO(1L, "c1","n1"));
        when(service.getAll()).thenReturn(samplePropertyTypes);
        ResponseEntity<List<PropertyTypeDTO>> result = controller.getAllPropertyTypes();
        verify(service, times(1)).getAll();
        assertNotNull(result);
        Cache applicationCache = cacheManager.getCache("propertyTypes");
        assertNotNull(applicationCache);
       
    }
    @Test
    public void testGetPropertyTypeByName_CacheHits() {
        String propertyName = "n1";
        List<PropertyTypeDTO> samplePropertyTypes = new ArrayList<>();
        samplePropertyTypes.add(new PropertyTypeDTO(1L, "c1","n1"));
        when(service.getPropertyTypeByName(propertyName)).thenReturn(samplePropertyTypes);
        ResponseEntity<List<PropertyTypeDTO>> result = controller.getPropertyTypeByName(propertyName);
        verify(service, times(1)).getPropertyTypeByName(propertyName);
        assertNotNull(result);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(propertyName);
        assertNotNull(applicationCache);
      
    }
    @Test
    public void testGetPropertyTypeByName_CacheFailure() {
        String invalidPropertyName = "invalidProperty";
        when(service.getPropertyTypeByName(invalidPropertyName)).thenThrow(new RuntimeException("PropertyType not exist"));
        assertThrows(RuntimeException.class, () -> controller.getPropertyTypeByName(invalidPropertyName));
        verify(service, times(1)).getPropertyTypeByName(invalidPropertyName);
        ValueWrapper applicationCache = cacheManager.getCache("propertyTypes").get(invalidPropertyName);
        assertNull(applicationCache); 
    }
    

	

}
