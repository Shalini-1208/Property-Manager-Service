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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.service.PropertyTypeService;

@SpringBootTest
class PropertyTypeControllerTest {
	
	@Mock
    private PropertyTypeService service;

    @InjectMocks
    private PropertyTypeController controller;

    @Test
    public void testCreatePropertyType() {
      
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.createPropertyType(propertyTypeDTO)).thenReturn(propertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.createPropertyType(propertyTypeDTO);
        verify(service, times(1)).createPropertyType(propertyTypeDTO);
        assertNotNull(result);
    }
    
    @Test
    void testCreatePropertyType_InvalidName() {
    
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"c1","");
        when(controller.createPropertyType(propertyTypeDTO)).thenThrow(new RuntimeException("PropertyType name is required"));
        verify(service, never()).createPropertyType(propertyTypeDTO);
    }
    @Test
    void testCreatePropertyType_InvalidClass() {
    
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"","n1");
        when(controller.createPropertyType(propertyTypeDTO)).thenThrow(new RuntimeException("PropertyType class is required"));
        verify(service, never()).createPropertyType(propertyTypeDTO);
    }
    @Test
    void testCreatePropertyType_DuplicateId() {
    
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.createPropertyType(propertyTypeDTO)).thenThrow(new RuntimeException("PropertyType already exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.createPropertyType(propertyTypeDTO));
        assertEquals("PropertyType already exist", exception.getMessage());
        verify(service, times(1)).createPropertyType(propertyTypeDTO);
        
    }
    
    @Test
    public void testUpdatePropertyType() {
        Long propertyTypeId = 1L;
        PropertyTypeDTO samplePropertyType = new PropertyTypeDTO(1L,"c1","n1");
        when(service.UpdatePropertyType(propertyTypeId, samplePropertyType)).thenReturn(samplePropertyType);
        ResponseEntity<PropertyTypeDTO> result = controller.updatePropertyType(propertyTypeId, samplePropertyType);
        verify(service, times(1)).UpdatePropertyType(propertyTypeId, samplePropertyType);
        assertNotNull(result);
        assertEquals(samplePropertyType, result.getBody());
        verify(service, times(1)).UpdatePropertyType(propertyTypeId, samplePropertyType);
        
    }
    
    @Test
    public void testUpdatePropertyType_WithInvalidId() {
        Long invalidId = -1L;
        PropertyTypeDTO samplePropertyTypeDTO = new PropertyTypeDTO(1L,"c1","n1");
        when(service.UpdatePropertyType(invalidId, samplePropertyTypeDTO)).thenThrow(new RuntimeException("PropertyType not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.updatePropertyType(invalidId, samplePropertyTypeDTO));
        assertEquals("PropertyType not exist", exception.getMessage());
        verify(service, times(1)).UpdatePropertyType(invalidId, samplePropertyTypeDTO);

    }
    @Test
    void testGetPropertyTypeById() {
        long id = 1L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        when(service.getPropertyTypeByID(id)).thenReturn(propertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.getPropertyTypeById(id);
        verify(service, times(1)).getPropertyTypeByID(id);
        assertEquals(propertyTypeDTO, result.getBody());
    }
    
    @Test
    void testGetPropertyTypeById_InvalidId() {
        long id = 1L;
        when(service.getPropertyTypeByID(id)).thenThrow(new RuntimeException("PropertyType not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getPropertyTypeById(id));
        assertEquals("PropertyType not exist", exception.getMessage());
        verify(service, times(1)).getPropertyTypeByID(id);
    }
    @Test
    void testDeletePropertyTypeById() {
        long id = 1L;
        controller.deletePropertyById(id);
        verify(service, times(1)).removePropertyTypeByID(id);
    }
    
    @Test
    void testDeletePropertyTypeById_InvalidId() {
        long id = 1L;
        doThrow(new RuntimeException("PropertyType not exist")).when(service).removePropertyTypeByID(id);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.deletePropertyById(id));
        assert(exception.getMessage().equals("PropertyType not exist"));
        verify(service, times(1)).removePropertyTypeByID(id);
    }
    @Test
    public void testGetByPropertyTypeIdAndName() {
       
        Long propertyTypeId = 1L;
        String propertyName = "sampleProperty";
        PropertyTypeDTO samplePropertyTypeDTO = new PropertyTypeDTO(1L, "c1","n1");
        when(service.getByPropertyTypeIdAndName(propertyTypeId, propertyName)).thenReturn(samplePropertyTypeDTO);
        ResponseEntity<PropertyTypeDTO> result = controller.getByPropertyTypeIdAndName(propertyTypeId, propertyName);
        verify(service, times(1)).getByPropertyTypeIdAndName(propertyTypeId, propertyName);
        assertNotNull(result);
    
    }
    @Test
    void testGetByPropertyTypeIdAndName_InvalidData() {
        
        long id = 1L;
        String name = "TestName";
        doThrow(new RuntimeException("PropertyType not exist")).when(service).getByPropertyTypeIdAndName(id, name);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getByPropertyTypeIdAndName(id, name));
        assert(exception.getMessage().equals("PropertyType not exist"));
        verify(service, times(1)).getByPropertyTypeIdAndName(id, name);
    }
    
    @Test
    public void testGetAllPropertyTypes() {
        List<PropertyTypeDTO> samplePropertyTypes = new ArrayList<>();
        samplePropertyTypes.add(new PropertyTypeDTO(1L, "c1","n1"));
        when(service.getAll()).thenReturn(samplePropertyTypes);
        ResponseEntity<List<PropertyTypeDTO>> result = controller.getAllPropertyTypes();
        verify(service, times(1)).getAll();
        assertNotNull(result);
        assertEquals(samplePropertyTypes.size(), result.getBody().size());
       
    }
    @Test
    public void testGetPropertyTypeByName() {
        String propertyName = "n1";
        List<PropertyTypeDTO> samplePropertyTypes = new ArrayList<>();
        samplePropertyTypes.add(new PropertyTypeDTO(1L, "c1","n1"));
        when(service.getPropertyTypeByName(propertyName)).thenReturn(samplePropertyTypes);
        ResponseEntity<List<PropertyTypeDTO>> result = controller.getPropertyTypeByName(propertyName);
        verify(service, times(1)).getPropertyTypeByName(propertyName);
        assertNotNull(result);
        assertEquals(samplePropertyTypes.size(), result.getBody().size());
      
    }
    @Test
    public void testGetPropertyTypeByName_WithInvalidName() {
        String invalidPropertyName = "invalidProperty";
        when(service.getPropertyTypeByName(invalidPropertyName)).thenThrow(new RuntimeException("PropertyType not exist"));
        assertThrows(RuntimeException.class, () -> controller.getPropertyTypeByName(invalidPropertyName));
        verify(service, times(1)).getPropertyTypeByName(invalidPropertyName);
    }
    
}
