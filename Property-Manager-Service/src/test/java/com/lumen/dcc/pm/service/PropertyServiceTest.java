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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;
import com.lumen.dcc.pm.repository.PropertyRepository;
import com.lumen.dcc.pm.transformer.PropertyTransformer;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class PropertyServiceTest {

	
	    @Autowired
	    private PropertyService propertyService;

	    @MockBean
	    private PropertyRepository propertyRepository;

	    @MockBean
	    private PropertyTransformer propertyTransformer;
	    
	    
	    ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
	    Application appEntity = new Application(1L,"shalini","it is name");
	    
        PropertyTypeDTO pty=new PropertyTypeDTO(1L,"c1","n1");
        PropertyType ptyEnt=new PropertyType(1L,"c1","n1");

	    @Test
	    void testCreateProperty_Success() {
	    	
	    	PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
	        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        when(propertyRepository.findById(1L)).thenReturn(null);
	        when(propertyTransformer.transformToEntity(propertyDTO)).thenReturn(property);
	        when(propertyRepository.save(property)).thenReturn(property);
	        when(propertyTransformer.transformToDto(property)).thenReturn(propertyDTO);
	        PropertyDTO result = propertyService.createProperty(propertyDTO);
	        assertNotNull(result);
	        assertEquals(propertyDTO.getPropertyId(), result.getPropertyId());
	        assertEquals(propertyDTO.getEngine(), result.getEngine());
	        assertEquals(propertyDTO.getEnvironment(), result.getEnvironment());
	        assertEquals(propertyDTO.getPropertyName(), result.getPropertyName());
	        assertEquals(propertyDTO.getPropertyValue(), result.getPropertyValue());
	        verify(propertyRepository, times(1)).save(property);
	    }
	    @Test
	    void testCreateProperty_TransformationFailure() {
	    	
	    	PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
	    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        when(propertyRepository.existsById(1L)).thenReturn(true);
	        assertThrows(RuntimeException.class, () -> propertyService.createProperty(propertyDTO));
	        verify(propertyRepository, never()).save(property);
	    }
	    
	    
	    @Test
	    void testGetPropertyByID_Success() {
	    	PropertyDTO expectedPropertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
	    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        long id=1L;
	        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
	        when(propertyRepository.getById(id)).thenReturn(property);
	        when(propertyTransformer.transformToDto(property)).thenReturn(expectedPropertyDTO);
	        PropertyDTO retrievedDto = propertyService.getPropertyByID(id);
	        assertEquals(expectedPropertyDTO, retrievedDto); 
	    }
	   
	    @Test
	    void testGetPropertyByID_PropertyNotFound() {
	
	        Long propertyId = 1L;
	    	Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	    	 when(propertyRepository.findById(1L)).thenReturn(null);
	        assertThrows(RuntimeException.class, () -> propertyService.getPropertyByID(propertyId));
	        verify(propertyTransformer, never()).transformToDto(property); 
	    }
	    
	    @Test
	    void testGetAll_Success() {
	       
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
	        for (int i = 0; i < result.size(); i++) {
	            assertEquals(expectedDTOs.get(i), result.get(i)); 
	        }
	    }
	    
	    @Test
	    void testGetAll_NoData() {
	     
	        when(propertyRepository.findAll()).thenReturn(Collections.emptyList());
	        RuntimeException exception = assertThrows(RuntimeException.class, () -> propertyService.getAll());
	        assertEquals("Property not exist", exception.getMessage());
	        verify(propertyRepository, times(1)).findAll();
	    }
	    @Test
	    void testUpdateProperty_Success() {
	     
	        Long propertyId = 1L;
	        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
	   
	        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
	        when(propertyRepository.save(property)).thenReturn(property);
	        when(propertyTransformer.transformToEntity(propertyDTO)).thenReturn(property);
	        when(propertyTransformer.transformToDto(property)).thenReturn(propertyDTO);
	        PropertyDTO result = propertyService.UpdateProperty(propertyId, propertyDTO);
	        assertNotNull(result);
	        assertEquals(propertyDTO.getPropertyId(), result.getPropertyId());
	        assertEquals(propertyDTO.getEngine(), result.getEngine());
	        assertEquals(propertyDTO.getEnvironment(), result.getEnvironment());
	        assertEquals(propertyDTO.getPropertyName(), result.getPropertyName());
	        assertEquals(propertyDTO.getPropertyValue(), result.getPropertyValue());
	        verify(propertyRepository, times(1)).findById(propertyId);
	        verify(propertyRepository, times(1)).save(property);
	    }
	    
	    @Test
	    void testUpdateProperty_PropertyNotFound() {
	     
	        Long propertyId = 1L;
	        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        PropertyDTO propertyDTO = new PropertyDTO(1L,appDto,"e1","dev",pty,"n1","v1");
	        when(propertyRepository.findById(1L)).thenReturn(null); 
	        assertThrows(RuntimeException.class, () -> propertyService.UpdateProperty(propertyId, propertyDTO));
	        verify(propertyRepository, times(1)).findById(propertyId);
	        verify(propertyRepository, never()).save(property);
	    }
	    
	    @Test
	    void testRemovePropertyByID_Success() {
	     
	        Long propertyId = 1L;
	        Property property = new Property(1L,appEntity,"e1","dev",ptyEnt,"n1","v1");
	        property.setPropertyId(propertyId);
	        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
	        propertyService.removePropertyByID(propertyId);
	        verify(propertyRepository, times(1)).findById(propertyId);
	        verify(propertyRepository, times(1)).deleteById(propertyId);
	    }
	    
	    @Test
	    void testRemovePropertyByID_PropertyNotFound() {
	       
	        Long propertyId = 1L;
	        when(propertyRepository.findById(1L)).thenReturn(null);
	        assertThrows(RuntimeException.class, () -> propertyService.removePropertyByID(propertyId));
	        verify(propertyRepository, times(1)).findById(propertyId);
	        verify(propertyRepository, never()).deleteById(propertyId);
	    }
	    
	    @Test
	    void testGetByPropertyNameAndEngineAndEnvironment_Success() {
	     
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
	        assertEquals(expectedPropertyDTO.get(0).getPropertyId(), result.get(0).getPropertyId());
	        assertEquals(expectedPropertyDTO.get(0).getEngine(), result.get(0).getEngine());
	        assertEquals(expectedPropertyDTO.get(0).getEnvironment(), result.get(0).getEnvironment());
	        assertEquals(expectedPropertyDTO.get(0).getPropertyName(), result.get(0).getPropertyName());
	        verify(propertyRepository, times(2)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	    }
	    @Test
	    void testGetByPropertyNameAndEngineAndEnvironment_PropertyNotFound() {
	       
	        String propertyName = "Property Name";
	        String engine = "Engine";
	        String environment = "Environment";
	        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(null);
	        assertThrows(RuntimeException.class, () -> propertyService.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
	        verify(propertyRepository, times(1)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	    }
	    
	    @Test
	    void testGetByEngineAndEnvironment_Success() {
	    	
	       
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
	        assertEquals(expectedPropertyDTO.get(0).getPropertyId(), result.get(0).getPropertyId());
	        assertEquals(expectedPropertyDTO.get(0).getEngine(), result.get(0).getEngine());
	        assertEquals(expectedPropertyDTO.get(0).getEnvironment(), result.get(0).getEnvironment());
	        assertNotNull(result);
	        assertSame(expectedPropertyDTO, result);
	        verify(propertyRepository, times(4)).getByEngineAndEnvironment(engine, environment);
	    }
	    @Test
	    void testGetByEngineAndEnvironment_PropertyNotFound() {
	   
	        String engine = "Engine";
	        String environment = "Environment";
	        when(propertyRepository.getByEngineAndEnvironment(engine, environment)).thenReturn(null);
	        assertThrows(RuntimeException.class, () -> propertyService.getByEngineAndEnvironment(engine, environment));
	        verify(propertyRepository, times(1)).getByEngineAndEnvironment(engine, environment);
	    }
	    @Test
	    void testGetByEngine_Success() {
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
	        for (int i = 0; i < result.size(); i++) {
	            assertEquals(expectedDTOs.get(i), result.get(i)); 
	        }
	        verify(propertyRepository, times(2)).getByEngine(engine);
	    }
	    
	    @Test
	    void testGetByEngine_NoPropertiesFound() {
	        String engine = "e2";
	        when(propertyRepository.getByEngine(engine)).thenReturn(Collections.emptyList());
	        assertThrows(RuntimeException.class, () -> propertyService.getByEngine(engine));
	        verify(propertyRepository, times(1)).getByEngine(engine);
	      
	    }
	    
	    @Test
	    void testGetByEnvironment_Success() {
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
	        for (int i = 0; i < result.size(); i++) {
	            assertEquals(expectedDTOs.get(i), result.get(i)); 
	        }
	        verify(propertyRepository, times(2)).getByEnvironment(envi);
	    }
	    
	    @Test
	    void testGetByEnvironment_NoPropertiesFound() {
	     
	        String envi = "devsss";
	        when(propertyRepository.getByEnvironment(envi)).thenReturn(Collections.emptyList());
	        assertThrows(RuntimeException.class, () -> propertyService.getByEnvironment(envi));
	        verify(propertyRepository, times(1)).getByEnvironment(envi);
	    }
	    
	    @Test
	    void testDeleteAllByPropertyNameAndEngineAndEnvironment_Success() {
	       
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
	    }
	    
	    @Test
	    void testDeleteAllByPropertyNameAndEngineAndEnvironment_PropertyNotFound() {
	        
	        String propertyName = "Property Name";
	        String engine = "Engine";
	        String environment = "Environment";
	        when(propertyRepository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)).thenReturn(null);
	        assertThrows(RuntimeException.class, () -> propertyService.deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
	        verify(propertyRepository, times(1)).getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	        verify(propertyRepository, never()).deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	    }
	}
