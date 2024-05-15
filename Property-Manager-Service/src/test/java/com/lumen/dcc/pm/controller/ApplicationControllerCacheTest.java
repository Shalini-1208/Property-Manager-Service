package com.lumen.dcc.pm.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.service.ApplicationService;



@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableCaching
public class ApplicationControllerCacheTest {


    @MockBean
    private ApplicationService service;

    @Autowired
    private CacheManager cacheManager;
    
    @InjectMocks
    private ApplicationController controller;

    @Test
    public void testCreateApplication_CachePut() throws Exception {
    	ApplicationDTO applicationDTO = new ApplicationDTO(1,"shalini","it is name");
        when(service.createApplication(applicationDTO)).thenReturn(applicationDTO);
        ResponseEntity<ApplicationDTO> result = controller.createApplication(applicationDTO);
        assertNotNull(result);
        verify(service, times(1)).createApplication(applicationDTO);
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache);  
    }
    
    @Test
    public void testCreateApplication_CacheFailure() throws Exception {
    	ApplicationDTO applicationDTO = new ApplicationDTO(1,"shalini","it is name");
        when(service.createApplication(applicationDTO)).thenReturn(null);
        ResponseEntity<ApplicationDTO> result = controller.createApplication(applicationDTO);
        verify(service, times(1)).createApplication(applicationDTO);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(1);
        assertNull(applicationCache);  
    }
    
    @Test
    void testUpdateApplication_CachePut() {
        Long id = 1L;
        ApplicationDTO applicationDTO = new ApplicationDTO(1,"shalini","it is name");
        when(service.UpdateApplication(id,  applicationDTO)).thenReturn(applicationDTO);
        ResponseEntity<ApplicationDTO> result = controller.updateApplication(id, applicationDTO);
        assertNotNull(result);
        verify(service, times(1)).UpdateApplication(id, applicationDTO);
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache);
    }
    @Test
    void testGetApplicationById_CachePut() {
        Long id = 1L;
        ApplicationDTO appDTO = new ApplicationDTO(1,"shalini","it is name");
        when(service.getApplicationByID(id)).thenReturn(appDTO);
        ResponseEntity<ApplicationDTO> result = controller.getApplicationById(id);
        assertNotNull(result);
        verify(service, times(1)).getApplicationByID(id);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(id);
        assertNotNull(applicationCache); 
    }
    @Test
    void testGetApplicationById_CacheFailure() {
        Long invalidId = 100L;
        when(service.getApplicationByID(invalidId)).thenThrow(new RuntimeException("Application not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getApplicationById(invalidId));
        assertEquals("Application not exist", exception.getMessage());
        verify(service, times(1)).getApplicationByID(invalidId);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(invalidId);
        assertNull(applicationCache);  
    }
    
    @Test
    void testGetAllApplications_CacheHits() {
        ApplicationDTO appDTO1 = new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO appDTO2 = new ApplicationDTO(2,"shalini","it is name");
        List<ApplicationDTO> appDTOList = new ArrayList<>();
        appDTOList.add(appDTO1);
        appDTOList.add(appDTO2);
        when(service.getAll()).thenReturn(appDTOList);
        ResponseEntity<List<ApplicationDTO>> result = controller.getAllApplications();
        assertNotNull(result);
        verify(service, times(1)).getAll();
        Cache applicationCache = cacheManager.getCache("application");
        assertNotNull(applicationCache); 
    }
    
    @Test
    void testGetAllApplications_CacheFailure_EmptyList() {
       
        when(service.getAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<ApplicationDTO>> result =controller.getAllApplications();
        assertNotNull(result);
        verify(service, times(1)).getAll();
        ValueWrapper applicationCache = cacheManager.getCache("application").get(1);
        assertNull(applicationCache); 
    }
    @Test
    void testGetApplicationByName_CacheHits() {
    	ApplicationDTO appDTO1 = new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO appDTO2 = new ApplicationDTO(2,"shalini","it is name");
        List<ApplicationDTO> appDTOList = new ArrayList<>();
        appDTOList.add(appDTO1);
        appDTOList.add(appDTO2);
        String name = "shalini";
        when(service.getApplicationByName(name)).thenReturn(appDTOList);
        ResponseEntity<List<ApplicationDTO>> result = controller.getApplicationByName(name);
        verify(service, times(1)).getApplicationByName(name);
        assertEquals(appDTOList, result.getBody());
        ValueWrapper applicationCache = cacheManager.getCache("application").get(name);
        assertNotNull(applicationCache); 
    }
    @Test
    void testGetApplicationByName_CacheFailure() {
        String invalidName = "nonExistingName";
        when(service.getApplicationByName(invalidName)).thenThrow(new RuntimeException("Application not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getApplicationByName(invalidName));
        assertEquals("Application not exist", exception.getMessage());
        verify(service, times(1)).getApplicationByName(invalidName);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(invalidName);
        assertNull(applicationCache);
    }
    @Test
    void testDeleteApplicationById_CacheHits() {
        Long id = 1L;
        controller.deleteApplicationById(id);
        verify(service, times(1)).removeApplicationByID(id);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(id);
        assertNotNull(applicationCache); 
    }
    @Test
    void testDeleteApplicationById_cacheFailure() {
        Long invalidId = 100L;
        doThrow(new RuntimeException("Application not exist")).when(service).removeApplicationByID(invalidId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.deleteApplicationById(invalidId));
        assertEquals("Application not exist", exception.getMessage());
        verify(service, times(1)).removeApplicationByID(invalidId);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(1);
        assertNull(applicationCache);
    }
    @Test
    void testDeleteApplicationByName_cacheHits() {
        String name = "shalini";
        controller.deleteApplicationByName(name);
        verify(service, times(1)).removeApplicationByName(name);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(name);
        assertNotNull(applicationCache); 
    }
    
    @Test
    void testDeleteApplicationByName_CacheFailure() {
        String invalidName = "nonExistingName";
        doThrow(new RuntimeException("Application not exist")).when(service).removeApplicationByName(invalidName);
        assertThrows(RuntimeException.class, () -> controller.deleteApplicationByName(invalidName));
        verify(service, times(1)).removeApplicationByName(invalidName);
        ValueWrapper applicationCache = cacheManager.getCache("application").get(invalidName);
        assertNull(applicationCache);
    }

}
