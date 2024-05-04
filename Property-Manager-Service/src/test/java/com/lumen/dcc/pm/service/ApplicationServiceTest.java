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
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.repository.ApplicationRepository;
import com.lumen.dcc.pm.transformer.ApplicationTransformer;

@SpringBootTest
class ApplicationServiceTest {

	@MockBean
    private ApplicationRepository repository;

    @MockBean
    private ApplicationTransformer transformer;

    @Autowired
    private ApplicationService service;
    
    @MockBean
    private Application app;

    @Test
    void testCreateApplication_Success() {
     
       
        Application appEntity = new Application(1L,"shalini","it is name");
        ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");

        when(transformer.transformToEntity(appDto)).thenReturn(appEntity);
        when(repository.findById(appDto.getApplicationId())).thenReturn(null);
        when(repository.save(appEntity)).thenReturn(appEntity);
        when(transformer.transformToDto(appEntity)).thenReturn(appDto);
        when(transformer.transformToEntity(appDto)).thenReturn(appEntity);
        ApplicationDTO createdAppDto = service.createApplication(appDto);
        assertEquals(appDto, createdAppDto);
    }

    @Test
    void testCreateApplication_Duplicates()
    {
    	 ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
    	 Application appEntity = new Application(1L,"shalini","it is name");
    	 when(repository.existsById(1L)).thenReturn(true);
         RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createApplication(appDto));
         assertEquals("id already exist", exception.getMessage());
         verify(repository, times(1)).existsById(1L);
         verify(repository, never()).save(appEntity);
    }
    
    @Test
    void testGetApplicationByID_Success() {

    	Application appEntity = new Application(1L,"shalini","it is name");
        ApplicationDTO appDto = new ApplicationDTO(1L,"shalini","it is name");
        when(repository.findById(1L)).thenReturn(Optional.of(appEntity));
        when(transformer.transformToDto(appEntity)).thenReturn(appDto);
        when(repository.getById(1L)).thenReturn(appEntity);
        ApplicationDTO retrievedDto = service.getApplicationByID(1L);
        assertEquals(appDto, retrievedDto); 
    }
    
    @Test
    void testGetApplicationByID_InvalidId() {

        Long id = 1L;
        Application application = new Application(1L,"shalini","it is name"); 
        when(repository.findById(id)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getApplicationByID(id));
        assertEquals("Application not exist", exception.getMessage());
        verify(transformer, never()).transformToDto(application);
        verify(repository, never()).getById(id);
    }
    
    
    @Test
    void testRemoveApplicationByID_Success() {

        Long id = 1L;
        Application application = new Application(1L,"shalini","it is name"); 
        when(repository.findById(id)).thenReturn(Optional.of(application));
        assertDoesNotThrow(() -> service.removeApplicationByID(id));
        verify(repository).deleteById(id);
    }

    @Test
    void testRemoveApplicationByID_InvalidId() {
   
        Long id = 1L;
        when(repository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.removeApplicationByID(id));
        verify(repository, never()).deleteById(id);
    }
    @Test
    void testGetApplicationByName_Success() {
    	
    	List<Application> applications = new ArrayList<>();
        Application app1 = new Application(1L,"shalini","it is name");
        Application app2 = new Application(2L,"shalini","it is name");
        applications.add(app1);
        applications.add(app2);
        List<ApplicationDTO> expectedDTOs = new ArrayList<>();
        ApplicationDTO dto1= new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO dto2= new ApplicationDTO(2L,"shalini","it is name");
        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);
        
        when(repository.getByName("shalini")).thenReturn(applications);
        when(transformer.transformToDto(applications)).thenReturn(expectedDTOs);
        List<ApplicationDTO> retrievedDto = service.getApplicationByName("shalini");
        assertEquals(expectedDTOs, retrievedDto); 
    }

    @Test
    void testGetApplicationByName_InvalidName() {
     
    	Application appEntity = new Application(1L,"shalini","it is name");
        String name = "InvalidName";
        when(repository.getByName(name)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> service.getApplicationByName(name));
        verify(transformer, never()).transformToDto(appEntity);
    }
    
    
    @Test
    void testRemoveApplicationByName_Success() {
    	List<Application> applications = new ArrayList<>();
        Application app1 = new Application(1L,"shalini","it is name");
        Application app2 = new Application(2L,"shalini","it is name");
        applications.add(app1);
        applications.add(app2);
        when(repository.getByName("shalini")).thenReturn(applications);
        assertDoesNotThrow(() -> service.removeApplicationByName("shalini"));
        verify(repository).deleteByName("shalini");
    }
    
    @Test
    void testRemoveApplicationByName_InvalidName() {
      
        String name = "InvalidName";
        when(repository.getByName(name)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> service.removeApplicationByName(name));
        verify(repository, never()).deleteByName(name);
    }
    @Test
    void testGetAll_Success() {
       
        List<Application> applications = new ArrayList<>();
        Application app1 = new Application(1L,"shalini","it is name");
        Application app2 = new Application(2L,"shalini","it is name");
        applications.add(app1);
        applications.add(app2);

        List<ApplicationDTO> expectedDTOs = new ArrayList<>();
        ApplicationDTO dto1= new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO dto2= new ApplicationDTO(2L,"shalini","it is name");
        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);


        when(repository.findAll()).thenReturn(applications);
        when(transformer.transformToDto(applications)).thenReturn(expectedDTOs);
        List<ApplicationDTO> result = service.getAll();
        assertEquals(expectedDTOs.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expectedDTOs.get(i), result.get(i)); 
        }
    }
    @Test
    void testGetAll_NoData() {
        
        when(repository.findAll()).thenReturn(Collections.emptyList());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getAll());
        assertEquals("Application not exist", exception.getMessage());
        verify(repository, times(1)).findAll();
    }
     
    @Test
    void testUpdateApplication_Success() {
        Long id = 1L;
        Application application = new Application(1L,"shalini","it is name");
        ApplicationDTO updatedDto = new ApplicationDTO(1L,"shalini","it is name");
       
        when(repository.findById(id)).thenReturn(Optional.of(application));
        when(repository.getById(id)).thenReturn(application);
        when(repository.save(application)).thenReturn(application);
        when(transformer.transformToDto(application)).thenReturn(updatedDto);
        when(transformer.transformToEntity(updatedDto)).thenReturn(application);
        ApplicationDTO result = service.UpdateApplication(id, updatedDto);
        assertEquals(updatedDto, result);
        
    }

    @Test
    void testUpdateApplication_InvalidId() {
     
        Long id = 1L;
        when(repository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.UpdateApplication(id, new ApplicationDTO()));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).getById(id);
        
    }

}
