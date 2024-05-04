package com.lumen.dcc.pm.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
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
import com.lumen.dcc.pm.service.ApplicationService;

@SpringBootTest
class ApplicationControllerTest {

   
    
    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private ApplicationService applicationService;
    
    @Test
    void testCreateApplication_Success() {
       
    	ApplicationDTO applicationDTO = new ApplicationDTO(1,"shalini","it is name");
        when(applicationService.createApplication(applicationDTO)).thenReturn(applicationDTO);
        ResponseEntity<ApplicationDTO> result = applicationController.createApplication(applicationDTO);
        assertNotNull(result);
        assertEquals(applicationDTO, result.getBody());
        verify(applicationService, times(1)).createApplication(applicationDTO);
    }
    
    @Test
    void testCreateApplication_InvalidName() {
      
        ApplicationDTO appDTO = new ApplicationDTO(1L,"","it is name");
        when(applicationController.createApplication(appDTO)).thenThrow(new RuntimeException("Application name is required"));
        verify(applicationService, never()).createApplication(appDTO);
    }
    @Test
    void testCreateApplication_InvalidDescription() {
      
        ApplicationDTO appDTO = new ApplicationDTO(1L,"shalini","");
        when(applicationController.createApplication(appDTO)).thenThrow(new RuntimeException("Application description is required"));
        verify(applicationService, never()).createApplication(appDTO);
    }
    @Test
    void testCreateApplication_DuplicateId() {
      
        ApplicationDTO appDTO = new ApplicationDTO(1L,"shalini","it is name");
        when(applicationService.createApplication(appDTO)).thenThrow(new RuntimeException("Application already exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> applicationController.createApplication(appDTO));
        assertEquals("Application already exist", exception.getMessage());
        verify(applicationService, times(1)).createApplication(appDTO);
    }
    @Test
    void testUpdateApplication_Success() {
  
        Long id = 1L;
        ApplicationDTO applicationDTO = new ApplicationDTO(1,"shalini","it is name");
        when(applicationService.UpdateApplication(id,  applicationDTO)).thenReturn(applicationDTO);
        ResponseEntity<ApplicationDTO> result = applicationController.updateApplication(id, applicationDTO);
        assertNotNull(result);
        assertEquals(applicationDTO, result.getBody());
        verify(applicationService, times(1)).UpdateApplication(id, applicationDTO);
    }
    
    @Test
    void testGetApplicationById_Success() {
        Long id = 1L;
        ApplicationDTO appDTO = new ApplicationDTO(1,"shalini","it is name");
        when(applicationService.getApplicationByID(id)).thenReturn(appDTO);
        ResponseEntity<ApplicationDTO> result = applicationController.getApplicationById(id);
        assertNotNull(result);
        assertEquals(appDTO, result.getBody());
        verify(applicationService, times(1)).getApplicationByID(id);
    }
    @Test
    void testGetApplicationById_Failure() {
       
        Long invalidId = 100L;
        when(applicationService.getApplicationByID(invalidId)).thenThrow(new RuntimeException("Application not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> applicationController.getApplicationById(invalidId));
        assertEquals("Application not exist", exception.getMessage());
        verify(applicationService, times(1)).getApplicationByID(invalidId);
    }
    @Test
    void testGetAllApplications_Success() {
        ApplicationDTO appDTO1 = new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO appDTO2 = new ApplicationDTO(2,"shalini","it is name");
        List<ApplicationDTO> appDTOList = new ArrayList<>();
        appDTOList.add(appDTO1);
        appDTOList.add(appDTO2);
        when(applicationService.getAll()).thenReturn(appDTOList);
        ResponseEntity<List<ApplicationDTO>> result = applicationController.getAllApplications();
        assertNotNull(result);
        assertEquals(2, result.getBody().size());
        verify(applicationService, times(1)).getAll();
    }
    @Test
    void testGetAllApplications_Failure_EmptyList() {
       
        when(applicationService.getAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<ApplicationDTO>> result = applicationController.getAllApplications();
        assertNotNull(result);
        assertTrue(result.getBody().isEmpty());
        verify(applicationService, times(1)).getAll();
    }
    @Test
    void testGetApplicationByName_Success() {
    	ApplicationDTO appDTO1 = new ApplicationDTO(1,"shalini","it is name");
        ApplicationDTO appDTO2 = new ApplicationDTO(2,"shalini","it is name");
        List<ApplicationDTO> appDTOList = new ArrayList<>();
        appDTOList.add(appDTO1);
        appDTOList.add(appDTO2);
        String name = "shalini";

        when(applicationService.getApplicationByName(name)).thenReturn(appDTOList);
        ResponseEntity<List<ApplicationDTO>> result = applicationController.getApplicationByName(name);
        verify(applicationService, times(1)).getApplicationByName(name);
        assertEquals(appDTOList, result.getBody());
    }
    @Test
    void testGetApplicationByName_InvalidName() {
        String invalidName = "nonExistingName";
        when(applicationService.getApplicationByName(invalidName)).thenThrow(new RuntimeException("Application not exist"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> applicationController.getApplicationByName(invalidName));
        assertEquals("Application not exist", exception.getMessage());
        verify(applicationService, times(1)).getApplicationByName(invalidName);
    }
    @Test
    void testDeleteApplicationById_Success() {
        
        Long id = 1L;
        applicationController.delteApplicationById(id);
        verify(applicationService, times(1)).removeApplicationByID(id);
    }
    @Test
    void testDeleteApplicationById_InvalidId() {

        Long invalidId = 100L;
        doThrow(new RuntimeException("Application not exist")).when(applicationService).removeApplicationByID(invalidId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> applicationController.delteApplicationById(invalidId));
        assertEquals("Application not exist", exception.getMessage());
        verify(applicationService, times(1)).removeApplicationByID(invalidId);
    }
    @Test
    void testDeleteApplicationByName_Success() {
       
        String name = "shalini";
        applicationController.delteApplicationByName(name);
        verify(applicationService, times(1)).removeApplicationByName(name);
    }
    @Test
    void testDeleteApplicationByName_InvalidName() {
        
        String invalidName = "nonExistingName";
        doThrow(new RuntimeException("Application not exist")).when(applicationService).removeApplicationByName(invalidName);
       assertThrows(RuntimeException.class, () -> applicationController.delteApplicationByName(invalidName));
        verify(applicationService, times(1)).removeApplicationByName(invalidName);
    }
}
    
   
