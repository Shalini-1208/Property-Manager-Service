package com.lumen.dcc.pm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.PropertyType;
import com.lumen.dcc.pm.repository.PropertyTypeRepository;
import com.lumen.dcc.pm.transformer.PropertyTypeTransformer;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class PropertyTypeServiceTest {

	@InjectMocks
    private PropertyTypeService propertyTypeService;

    @Mock
    private PropertyTypeRepository propertyTypeRepository;

    @Mock
    private PropertyTypeTransformer propertyTypeTransformer;

    @Test
    void testCreatePropertyType_Success() {
     
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L, "c1","n1");
        PropertyType propertyType=new PropertyType(1L,"c1,","n1");
        when(propertyTypeRepository.existsById(propertyTypeDTO.getPropertyTypeId())).thenReturn(false);
        when(propertyTypeTransformer.transformToEntity(propertyTypeDTO)).thenReturn(propertyType);
        when(propertyTypeTransformer.transformToDto(propertyType)).thenReturn(propertyTypeDTO);
        when(propertyTypeRepository.save(propertyType)).thenReturn(propertyType);
        PropertyTypeDTO result = propertyTypeService.createPropertyType(propertyTypeDTO);
        verify(propertyTypeRepository, times(1)).existsById(propertyTypeDTO.getPropertyTypeId());
        verify(propertyTypeTransformer, times(1)).transformToEntity(propertyTypeDTO);
        verify(propertyTypeRepository, times(1)).save(propertyType);
        verify(propertyTypeTransformer, times(1)).transformToDto(propertyType);
        assertEquals(propertyTypeDTO, result);
    }

    @Test
    void testCreatePropertyType_IdAlreadyExists() {
    	
    	PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(1L, "c1","n1");
        PropertyType propertyType=new PropertyType(1L,"c1,","n1");
        when(propertyTypeRepository.existsById(propertyTypeDTO.getPropertyTypeId())).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyTypeService.createPropertyType(propertyTypeDTO));
        assertEquals("id already exist", exception.getMessage());
        verify(propertyTypeRepository, times(1)).existsById(propertyTypeDTO.getPropertyTypeId());
        verifyNoInteractions(propertyTypeTransformer);
    }
    
    @Test
    void testGetPropertyTypeByID_Success() {
 
        long id = 262L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(262L, "c1","n1");
        PropertyType propertyType=new PropertyType(262L,"c1,","n1");
        when(propertyTypeRepository.findById(id)).thenReturn(Optional.of(propertyType));
        when(propertyTypeTransformer.transformToDto(propertyType)).thenReturn(propertyTypeDTO);
        when(propertyTypeRepository.getById(id)).thenReturn(propertyType);
        PropertyTypeDTO result = propertyTypeService.getPropertyTypeByID(id);
        verify(propertyTypeRepository, times(1)).findById(id);
        verify(propertyTypeTransformer, times(1)).transformToDto(propertyType);
        assertEquals(propertyTypeDTO, result);
    }
    
    @Test
    public void testGetAll_WithDataExists() {

        PropertyTypeDTO propertyTypeDTO1 = new PropertyTypeDTO(1L, "c1","n1");
        PropertyTypeDTO propertyTypeDTO2 = new PropertyTypeDTO(2L, "c1","n1");
        List<PropertyTypeDTO>dtos=new ArrayList<>();
        dtos.add(propertyTypeDTO1);
        dtos.add(propertyTypeDTO2);
        PropertyType propertyType1 = new PropertyType(1L, "c1","n1");
        PropertyType propertyType2 = new PropertyType(2L, "c1","n1");
        List<PropertyType>pt=new ArrayList<>();
        pt.add(propertyType1);
        pt.add(propertyType2);
        when(propertyTypeRepository.findAll()).thenReturn(pt);
        when(propertyTypeTransformer.transformToDto(pt)).thenReturn(dtos);
        List<PropertyTypeDTO> result = propertyTypeService.getAll();
        assertNotNull(result);
        assertEquals(dtos.size(), result.size());
        
    }

    @Test
    public void testGetAll_WithDataDoesNotExist() {
       
        when(propertyTypeRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> propertyTypeService.getAll());
        verify(propertyTypeRepository, times(1)).findAll();
        verifyNoInteractions(propertyTypeTransformer);
    }

    @Test
    void testGetPropertyTypeByID_IdNotFound() {
        long id = 1L;
        when(propertyTypeRepository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyTypeService.getPropertyTypeByID(id));
        verify(propertyTypeRepository, times(1)).findById(id);
        verifyNoInteractions(propertyTypeTransformer);
    }
    
    @Test
    void testUpdatePropertyType_Success() {
        long id = 1L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        PropertyType propertyType = new PropertyType(id, "c1","n1");
        when(propertyTypeRepository.findById(id)).thenReturn(Optional.of(propertyType));
        when(propertyTypeRepository.getById(id)).thenReturn(propertyType);
        when(propertyTypeTransformer.transformToEntity(propertyTypeDTO)).thenReturn(propertyType);
        when(propertyTypeTransformer.transformToDto(propertyType)).thenReturn(propertyTypeDTO);
        when(propertyTypeRepository.save(propertyType)).thenReturn(propertyType);
        PropertyTypeDTO result = propertyTypeService.UpdatePropertyType(id, propertyTypeDTO);
        assertEquals(propertyTypeDTO.getPropertyTypeId(), result.getPropertyTypeId());
        assertEquals(propertyTypeDTO.getName(), result.getName());
        assertEquals(propertyTypeDTO.getImplementationClass(), result.getImplementationClass());
        verify(propertyTypeRepository, times(1)).findById(id);
        verify(propertyTypeRepository, times(1)).save(propertyType);
        verify(propertyTypeTransformer, times(1)).transformToEntity(propertyTypeDTO);
        
    }

    @Test
    void testUpdatePropertyType_IdNotFound() {
        long id = 1L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        PropertyType propertyType = new PropertyType(id, "c1","n1");
        when(propertyTypeRepository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyTypeService.UpdatePropertyType(id,propertyTypeDTO));
        verify(propertyTypeRepository, times(1)).findById(id);
        verify(propertyTypeRepository, never()).save(propertyType);
    }
    @Test
    void testRemovePropertyTypeByID_Success() {
        long id = 1L;
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        PropertyType propertyType = new PropertyType(id, "c1","n1");
        when(propertyTypeRepository.findById(id)).thenReturn(Optional.of(propertyType));
        propertyTypeService.removePropertyTypeByID(id);
        verify(propertyTypeRepository, times(1)).findById(id);
        verify(propertyTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void testRemovePropertyTypeByID_IdNotFound() {
        long id = 1L;
        when(propertyTypeRepository.findById(id)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyTypeService.removePropertyTypeByID(id));
        assertEquals("PropertyType not exist", exception.getMessage());
        verify(propertyTypeRepository, times(1)).findById(id);
        verifyNoMoreInteractions(propertyTypeRepository);
    }
    @Test
    void testGetByPropertyTypeIdAndName_Success() {
        long id = 1L;
        String name = "TestType";
        PropertyTypeDTO propertyTypeDTO = new PropertyTypeDTO(id, "c1","n1");
        PropertyType propertyType = new PropertyType(id, "c1","n1");
        when(propertyTypeRepository.getByPropertyTypeIdAndName(id, name)).thenReturn(propertyType);
        when(propertyTypeTransformer.transformToDto(propertyType)).thenReturn(propertyTypeDTO);
        PropertyTypeDTO result = propertyTypeService.getByPropertyTypeIdAndName(id, name);
        verify(propertyTypeRepository, times(2)).getByPropertyTypeIdAndName(id, name);
        verify(propertyTypeTransformer, times(1)).transformToDto(propertyType);
        assertEquals(propertyTypeDTO, result);
    }

    @Test
    void testGetByPropertyTypeIdAndName_DataNotExist() {
        long id = 1L;
        String name = "NonExistentType";
        when(propertyTypeRepository.getByPropertyTypeIdAndName(id, name)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyTypeService.getByPropertyTypeIdAndName(id, name));
        assertEquals("PropertyType not exist", exception.getMessage());
        verifyNoInteractions(propertyTypeTransformer);
    }
    
    @Test
    public void testGetPropertyTypeByName_WhenNameExists() {
       
        
        PropertyTypeDTO propertyTypeDTO1 = new PropertyTypeDTO(1L, "c1","n1");
        PropertyTypeDTO propertyTypeDTO2 = new PropertyTypeDTO(2L, "c1","n1");
        List<PropertyTypeDTO>dtos=new ArrayList<>();
        dtos.add(propertyTypeDTO1);
        dtos.add(propertyTypeDTO2);
        PropertyType propertyType1 = new PropertyType(1L, "c1","n1");
        PropertyType propertyType2 = new PropertyType(2L, "c1","n1");
        List<PropertyType>pt=new ArrayList<>();
        pt.add(propertyType1);
        pt.add(propertyType2);
        String propertyName = "n1";
        when(propertyTypeRepository.getByName(propertyName)).thenReturn(pt);
        when(propertyTypeTransformer.transformToDto(pt)).thenReturn(dtos);
        List<PropertyTypeDTO> result = propertyTypeService.getPropertyTypeByName(propertyName);
        assertNotNull(result);
        assertEquals(dtos.get(0), result.get(0)); 
    }

    @Test
    public void testGetPropertyTypeByName_WhenNameDoesNotExist() {
       
        String nonExistingPropertyName = "nonExistingProperty";
        when(propertyTypeRepository.getByName(nonExistingPropertyName)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> propertyTypeService.getPropertyTypeByName(nonExistingPropertyName));
        verify(propertyTypeRepository, times(1)).getByName(nonExistingPropertyName);
        verifyNoInteractions(propertyTypeTransformer);
    }
    
    
    
    

}
