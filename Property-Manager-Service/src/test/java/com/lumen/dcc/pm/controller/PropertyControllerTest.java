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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;
import com.lumen.dcc.pm.service.PropertyService;

@SpringBootTest
class PropertyControllerTest {
	
   
    
    @InjectMocks
    private PropertyController propertyController;

    @Mock
    private PropertyService propertyService;
    
    ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
    Application appEntity = new Application(1L,"shalini","it is name");
    
    PropertyTypeDTO pty=new PropertyTypeDTO(1L,"c1","n1");
    PropertyType ptyEnt=new PropertyType(1L,"c1","n1");

    @Test
    void testCreateProperty_Success() {
    
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.createProperty(propertyDTO)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.createProperty(propertyDTO);
        assertNotNull(result);
        assertEquals(propertyDTO.getPropertyId(), result.getBody().getPropertyId());
        assertEquals(propertyDTO.getPropertyName(), result.getBody().getPropertyName());
        verify(propertyService, times(1)).createProperty(propertyDTO);
    }
    
    
    @Test
    void testCreateProperty_Duplicate() {
    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property already exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.createProperty(propertyDTO));
        assertEquals("Property already exist", exception.getMessage());
        verify(propertyService, times(1)).createProperty(propertyDTO);
    }
    
    @Test
    void testCreateProperty_InvalidEngine() {
      
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"","dev",pty,"n1","v1");
        when(propertyController.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property engine is required"));
        verify(propertyService, never()).createProperty(propertyDTO);
    }
    @Test
    void testCreateProperty_InvalidEnvironment() {
      
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","",pty,"n1","v1");
        when(propertyController.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property environment is required"));
        verify(propertyService, never()).createProperty(propertyDTO);
    }
    @Test
    void testCreateProperty_InvalidPropertyName() {
      
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"","v1");
        when(propertyController.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property name is required"));
        verify(propertyService, never()).createProperty(propertyDTO);
    }
    @Test
    void testCreateProperty_InvalidPropertyValue() {
      
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","");
        when(propertyController.createProperty(propertyDTO)).thenThrow(new RuntimeException("Property value is required"));
        verify(propertyService, never()).createProperty(propertyDTO);
    }
    
    @Test
    void testUpdateProperty_Success() {
    
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.UpdateProperty(propertyId, propertyDTO)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.updateProperty(propertyId, propertyDTO);
        assertNotNull(result);
        assertEquals(propertyId, result.getBody().getPropertyId());
        assertEquals(propertyDTO.getPropertyName(), result.getBody().getPropertyName());
        verify(propertyService, times(1)).UpdateProperty(propertyId, propertyDTO);
    }
    @Test
    void testUpdateProperty_Failure() {
      
        Long propertyId = 1L;
        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        propertyDTO.setPropertyName("Updated Property");
        when(propertyService.UpdateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Update failed"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.updateProperty(propertyId, propertyDTO));
        assertEquals("Update failed", exception.getMessage());
        verify(propertyService, times(1)).UpdateProperty(propertyId, propertyDTO);
    }
    @Test
    void testUpdateProperty_InvalidId() {
      
        Long invalidPropertyId = null;
        PropertyDTO validPropertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyController.updateProperty(invalidPropertyId, validPropertyDTO)).thenThrow(new RuntimeException("Property id should not be null"));
        verify(propertyService, never()).UpdateProperty(anyLong(), any(PropertyDTO.class));
    }
    @Test
    void testUpdateProperty_InvalidEngine() {
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"","dev",pty,"n1","v1");
        when(propertyController.updateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Property engine is required"));
        verify(propertyService, never()).UpdateProperty(anyLong(), any(PropertyDTO.class));
    }
    @Test
    void testUpdateProperty_InvalidEnvironment() {
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","",pty,"n1","v1");
        when(propertyController.updateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Property environment is required"));
        verify(propertyService, never()).UpdateProperty(anyLong(), any(PropertyDTO.class));
    }
    @Test
    void testUpdateProperty_InvalidPropertyName() {
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"","v1");
        when(propertyController.updateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Property name is required"));
        verify(propertyService, never()).UpdateProperty(anyLong(), any(PropertyDTO.class));
    }
    @Test
    void testUpdateProperty_InvalidPropertyValue() {
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","");
        when(propertyController.updateProperty(propertyId, propertyDTO)).thenThrow(new RuntimeException("Property value is required"));
        verify(propertyService, never()).UpdateProperty(anyLong(), any(PropertyDTO.class));
    }
    
    @Test
    void testGetPropertyById_Success() {
     
        Long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
        when(propertyService.getPropertyByID(propertyId)).thenReturn(propertyDTO);
        ResponseEntity<PropertyDTO> result = propertyController.getPropertyById(propertyId);
        assertNotNull(result);
        assertEquals(propertyId, result.getBody().getPropertyId());
        assertEquals(propertyDTO.getPropertyName(), result.getBody().getPropertyName());
        verify(propertyService, times(1)).getPropertyByID(propertyId);
    }
    
    @Test
    void testGetPropertyById_InvalidId() {
 
        Long invalidPropertyId = -1L;
        when(propertyService.getPropertyByID(invalidPropertyId)).thenThrow(new RuntimeException("Property id not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyById(invalidPropertyId));
        assertEquals("Property id not exist", exception.getMessage());
        verify(propertyService, times(1)).getPropertyByID(invalidPropertyId);
    }
    
    @Test
    void testGetAllProperties_Success() {

        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(2L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getAll()).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getAllProperties();
        assertNotNull(result);
        assertEquals(propertyDTOList.size(), result.getBody().size());
        assertEquals(propertyDTOList.get(0).getPropertyId(), result.getBody().get(0).getPropertyId());
        assertEquals(propertyDTOList.get(1).getPropertyName(), result.getBody().get(1).getPropertyName());
        verify(propertyService, times(1)).getAll();
    }
    @Test
    void testGetAllProperties_Failure_EmptyList() {
       
        when(propertyService.getAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<PropertyDTO>>result = propertyController.getAllProperties();
        assertNotNull(result);
        assertTrue(result.getBody().isEmpty());
        verify(propertyService, times(1)).getAll();
    }
    
    @Test
    void testDeletePropertyById_Success() {
  
        Long propertyId = 1L;
        propertyController.deletePropertyById(propertyId);
        verify(propertyService, times(1)).removePropertyByID(propertyId);
    }
    
    @Test
    void testDeletePropertyById_IdNotFound() {
      
        Long invalidPropertyId = -1L;
        doThrow(new RuntimeException("Property not found")).when(propertyService).removePropertyByID(invalidPropertyId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.deletePropertyById(invalidPropertyId));
        assertEquals("Property not found", exception.getMessage());
        verify(propertyService, times(1)).removePropertyByID(invalidPropertyId);
    }
    
    @Test
    void testGetPropertyByEngine_Success() {
      
        String engine = "e1";
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEngine(engine)).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEngine(engine);
        assertNotNull(result);
        assertEquals(propertyDTOList.size(), result.getBody().size());
        assertEquals(propertyDTOList.get(0).getPropertyId(), result.getBody().get(0).getPropertyId());
        assertEquals(propertyDTOList.get(1).getPropertyName(), result.getBody().get(1).getPropertyName());
        verify(propertyService, times(1)).getByEngine(engine);
    }
    
    @Test
    void testGetPropertyByEngine_Exception() {
        String engine = "TestEngine";
        when(propertyService.getByEngine(engine)).thenThrow(new RuntimeException("Property not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyByEngine(engine));
        assertEquals("Property not exist", exception.getMessage());
        verify(propertyService, times(1)).getByEngine(engine);
    }
    @Test
    void testGetPropertyByEnvironment_Success() {
      
        String envi = "dev";
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        propertyDTOList.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEnvironment(envi)).thenReturn(propertyDTOList);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEnvironment(envi);
        assertNotNull(result);
        assertEquals(propertyDTOList.size(), result.getBody().size());
        assertEquals(propertyDTOList.get(0).getPropertyId(), result.getBody().get(0).getPropertyId());
        assertEquals(propertyDTOList.get(1).getPropertyName(), result.getBody().get(1).getPropertyName());
        verify(propertyService, times(1)).getByEnvironment(envi);
    }
    
    @Test
    void testGetPropertyByEnvironment_Exception() {
        String envi = "TestEngine";
        when(propertyService.getByEnvironment(envi)).thenThrow(new RuntimeException("Property not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyController.getPropertyByEnvironment(envi));
        assertEquals("Property not exist", exception.getMessage());
        verify(propertyService, times(1)).getByEnvironment(envi);
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
        assertEquals(expectedPropertyDTO.get(0).getPropertyId(), result.getBody().get(0).getPropertyId());
        assertEquals(expectedPropertyDTO.get(0).getPropertyName(), result.getBody().get(0).getPropertyName());
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
    }
    @Test
    void testGetPropertyByEngineAndEnvironment_NoPropertyFound() {
       
        String engine = "TestEngine";
        String environment = "TestEnvironment";
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByEngineAndEnvironment(engine, environment);
        assertNull(result);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
    }
    
    @Test
    void testGetPropertyByNameAndEngineAndEnvironment_Success() {
    
        String name = "n1";
        String engine = "e1";
        String environment = "devt";
       
        List<PropertyDTO> expectedPropertyDTO = new ArrayList<>();
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        expectedPropertyDTO.add(new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1"));
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(expectedPropertyDTO);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByNameAndEngineAndEnvironment(name, engine, environment);
        assertNotNull(result);
        assertEquals(expectedPropertyDTO.get(0).getPropertyId(), result.getBody().get(0).getPropertyId());
        assertEquals(expectedPropertyDTO.get(0).getPropertyName(), result.getBody().get(0).getPropertyName());
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
    }
    
    @Test
    void testGetPropertyByNameAndEngineAndEnvironment_NoPropertyFound() {
        String name = "TestName";
        String engine = "TestEngine";
        String environment = "TestEnvironment";
        when(propertyService.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
        ResponseEntity<List<PropertyDTO>> result = propertyController.getPropertyByNameAndEngineAndEnvironment(name, engine, environment);
        assertNull(result);
        verify(propertyService, times(1)).getByEngineAndEnvironment(engine, environment);
    }
    
    @Test
    void testDeltePropertyByNameAndEngineAndEnvironment_Success() {
      
        String name = "n1";
        String engine = "e1";
        String environment = "dev";
        propertyController.deletePropertyByNameAndEngineAndEnvironment(name, engine, environment);
        verify(propertyService, times(1)).deleteAllByPropertyNameAndEngineAndEnvironment(name, engine, environment);
    }
    
    
    

}

  

