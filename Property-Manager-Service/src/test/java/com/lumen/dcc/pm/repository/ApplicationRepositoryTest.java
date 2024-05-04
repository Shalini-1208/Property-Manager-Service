package com.lumen.dcc.pm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lumen.dcc.pm.entity.Application;
@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationRepositoryTest {
		
		@Autowired
		private ApplicationRepository repo;
		
		private Application app1;
	    private Application app2;

	    @BeforeEach
	    void setUp() {
	      
	      app1 = new Application(1L,"shalini","it is a name");
	      app2 = new Application(2L,"shalini","it is a name");
	      repo.save(app1);
	      repo.save(app2);
	    }
	    @AfterEach
		void tests(){
			repo.delete(app1);
			repo.delete(app2);
		}
	    @Test
	    void testGetByName_Success() {
	    	Application apps=new Application(1000L,"paru","it is unique");
	    	repo.save(apps);
	        List<Application> result =repo.getByName("paru");
	        assertNotNull(result);
	    }

	    @Test
	    void testGetByName_NoResult() {
	        List<Application> result = repo.getByName("None");
	        assertEquals(0,result .size());
	    }
	    @Test
	    void testGetById_Success() {
	   
	        Application result =repo.getById(1L);
	        assertEquals(1L, result.getApplicationId());
	    }

	    @Test
	    void testGetById_NoResult() {
	        Application result = repo.getById(0L);
	        assertNotNull(result);
	    }

	    @Test
	    void testDeleteByName_Success() {
	        repo.deleteByName("abcd");
	        List<Application> res=repo.getByName("abcd");
	        assertEquals(0,res.size());
	   
	    }
	    @Test
	    void testSave() {
	        Application app3 = new Application(3L,"aaa","it is wrong");
	        repo.save(app3);
	        assertTrue(repo.count()>1);
	    }
	    
	    @Test
	    public void testSaveAll_Success() {
	       
	        List<Application> applicationsToSave = new ArrayList<>();
	        applicationsToSave.add(app1);
	        applicationsToSave.add(app2);
	        repo.saveAll(applicationsToSave); 
	        List<Application> savedApplications = repo.findAll();
	        Application savedApplication1 = savedApplications.get(0);
	        assertEquals("shalini", savedApplication1.getName());
	        Application savedApplication2 = savedApplications.get(1);
	        assertEquals("shalini", savedApplication2.getName());
	     
	    }	

	}



