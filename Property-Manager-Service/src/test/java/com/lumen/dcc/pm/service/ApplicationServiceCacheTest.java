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
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.repository.ApplicationRepository;
import com.lumen.dcc.pm.transformer.ApplicationTransformer;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableCaching
class ApplicationServiceCacheTest {
	
	@Autowired
    private CacheManager cacheManager;
	
	@MockBean
    private ApplicationRepository repository;

    @MockBean
    private ApplicationTransformer transformer;

    @Autowired
    private ApplicationService service;
    
    @MockBean
    private Application app;
    
    
    @Test
    void testCreateApplication_CacheHits() {
     
       
        Application appEntity = new Application(1L,"shalini","it is name");
        ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");

        when(transformer.transformToEntity(appDto)).thenReturn(appEntity);
        when(repository.findById(appDto.getApplicationId())).thenReturn(null);
        when(repository.save(appEntity)).thenReturn(appEntity);
        when(transformer.transformToDto(appEntity)).thenReturn(appDto);
        when(transformer.transformToEntity(appDto)).thenReturn(appEntity);
        ApplicationDTO createdAppDto = service.createApplication(appDto);
        assertEquals(appDto, createdAppDto);
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache);
    }

    @Test
    void testCreateApplication_CacheFailure()
    {
    	 ApplicationDTO appDto = new ApplicationDTO(1,"shalini","it is name");
    	 Application appEntity = new Application(1L,"shalini","it is name");
    	 when(repository.existsById(1L)).thenReturn(true);
         RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createApplication(appDto));
         assertEquals("id already exist", exception.getMessage());
         verify(repository, times(1)).existsById(1L);
         verify(repository, never()).save(appEntity);
         ValueWrapper applicationCache = cacheManager.getCache("application").get(1);
         assertNull(applicationCache); 
    }
    
    @Test
    void testGetApplicationByID_CacheHits() {

    	Application appEntity = new Application(1L,"shalini","it is name");
        ApplicationDTO appDto = new ApplicationDTO(1L,"shalini","it is name");
        when(repository.findById(1L)).thenReturn(Optional.of(appEntity));
        when(transformer.transformToDto(appEntity)).thenReturn(appDto);
        when(repository.getById(1L)).thenReturn(appEntity);
        ApplicationDTO retrievedDto = service.getApplicationByID(1L);
        assertEquals(appDto, retrievedDto); 
        ValueWrapper applicationCache = cacheManager.getCache("application").get(1);
        assertNotNull(applicationCache);
    }
    
    @Test
    void testGetApplicationByID_CacheFailure() {

        Long id = 1L;
        Application application = new Application(1L,"shalini","it is name"); 
        when(repository.findById(id)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getApplicationByID(id));
        assertEquals("Application not exist", exception.getMessage());
        verify(transformer, never()).transformToDto(application);
        verify(repository, never()).getById(id);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(id);
        assertNull(applicationCache); 
    }
    
    
    @Test
    void testRemoveApplicationByID_CacheHits() {

        Long id = 1L;
        Application application = new Application(1L,"shalini","it is name"); 
        when(repository.findById(id)).thenReturn(Optional.of(application));
        assertDoesNotThrow(() -> service.removeApplicationByID(id));
        verify(repository).deleteById(id);
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache);
    }

    @Test
    void testRemoveApplicationByID_CacheFailure() {
   
        Long id = 1L;
        when(repository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.removeApplicationByID(id));
        verify(repository, never()).deleteById(id);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(id);
        assertNull(applicationCache); 
    }
    @Test
    void testGetApplicationByName_CacheHits() {
    	
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
        ValueWrapper applicationCache = cacheManager.getCache("applications").get("shalini");
        assertNotNull(applicationCache);
    }

    @Test
    void testGetApplicationByName_CacheFailure() {
     
    	Application appEntity = new Application(1L,"shalini","it is name");
        String name = "InvalidName";
        when(repository.getByName(name)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> service.getApplicationByName(name));
        verify(transformer, never()).transformToDto(appEntity);
        ValueWrapper applicationCache = cacheManager.getCache("applications").get(name);
        assertNull(applicationCache); 
    }
    
    
    @Test
    void testRemoveApplicationByName_CacheHits() {
    	List<Application> applications = new ArrayList<>();
        Application app1 = new Application(1L,"shalini","it is name");
        Application app2 = new Application(2L,"shalini","it is name");
        applications.add(app1);
        applications.add(app2);
        when(repository.getByName("shalini")).thenReturn(applications);
        assertDoesNotThrow(() -> service.removeApplicationByName("shalini"));
        verify(repository).deleteByName("shalini");
        ValueWrapper applicationCache = cacheManager.getCache("applications").get("shalini");
        assertNotNull(applicationCache);
    }
    
    @Test
    void testRemoveApplicationByName_CacheFailure() {
      
        String name = "InvalidName";
        when(repository.getByName(name)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> service.removeApplicationByName(name));
        verify(repository, never()).deleteByName(name);
        ValueWrapper applicationCache = cacheManager.getCache("applications").get(name);
        assertNull(applicationCache); 
    }
    @Test
    void testGetAll_CacheHits() {
       
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
        Cache applicationCache = cacheManager.getCache("applications");
        assertNotNull(applicationCache);
    }
    @Test
    void testGetAll_CacheFailure() {
        
        when(repository.findAll()).thenReturn(Collections.emptyList());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getAll());
        assertEquals("Application not exist", exception.getMessage());
        verify(repository, times(1)).findAll();
        ValueWrapper applicationCache = cacheManager.getCache("applications").get(1);
        assertNull(applicationCache); 
    }
     
    @Test
    void testUpdateApplication_CacheHits() {
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
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache);
        
    }

    @Test
    void testUpdateApplication_CacheFailure() {
     
        Long id = 1L;
        when(repository.findById(id)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.UpdateApplication(id, new ApplicationDTO()));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).getById(id);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(id);
        assertNull(applicationCache); 
    }


	

}
