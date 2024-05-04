package com.lumen.dcc.pm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
class PropertyRepositoryTest {

	

		@Autowired
	    private PropertyRepository pr;
		@Autowired
	    private ApplicationRepository ar;
		@Autowired
	    private PropertyTypeRepository ptr;

	    private Property prop;
	    private PropertyType ptype;
	    private Application app;

	    @BeforeEach
	    void setUp() {
	    	app=new Application(767L,"shalini","it is a name");
	    	ar.save(app);
	    	ptype=new PropertyType(261L,"c1","n1");
	    	ptr.save(ptype);
	        prop = new Property(1L,app,"e1","dev",ptype,"n1","v1");
	        pr.save(prop);
	    }
		@AfterEach
		void test1(){
			pr.deleteAll();
		}
		@Test
	    public void testGetByPropertyNameAndEngineAndEnvironment_Success() {
	      
	        List<Property> retrievedProperty = pr.getByPropertyNameAndEngineAndEnvironment("n1", "e1", "dev");
	        assertNotNull(retrievedProperty);
	        assertEquals("n1", retrievedProperty.get(0).getPropertyName());
	        assertEquals("e1", retrievedProperty.get(0).getEngine());
	        assertEquals("dev", retrievedProperty.get(0).getEnvironment());
	    }

	    @Test
	    public void testGetByPropertyNameAndEngineAndEnvironment_NotFound() {
	       
	        List<Property> retrievedProperty = pr.getByPropertyNameAndEngineAndEnvironment("NonExistentProperty", "TestEngine", "TestEnvironment");
	        assertEquals(0,retrievedProperty .size());
	    }
	    
	    @Test
	    public void testGetByEngineAndEnvironment_Success() {
	    	
	        List<Property> retrievedProperties = pr.getByEngineAndEnvironment("e1", "dev");
	        assertNotNull(retrievedProperties);
	    }

	    @Test
	    public void testGetByEngineAndEnvironment_NotFound() {
	    	
	        List<Property> retrievedProperties = pr.getByEngineAndEnvironment("None", "Environment");
	        assertEquals(0,retrievedProperties .size());
	    }
	    @Test
	    @Transactional
	    public void testDeleteAllByPropertyNameAndEngineAndEnvironment_Success() {
	    	 prop = new Property(1L,app,"Innodb","devs",ptype,"abc","v1");
		     pr.save(prop);
	         pr.deleteAllByPropertyNameAndEngineAndEnvironment("abc", "Innodb", "devs");
	         List<Property> retrievedProperties = pr.getByPropertyNameAndEngineAndEnvironment("abc", "Innodb", "devs");
		     assertEquals(0,retrievedProperties .size());
	        
	       
	    }
	    @Test
	    public void testGetByEngine_Success() {
	       
	        List<Property> properties =pr.getByEngine("e1");
	        assertNotNull(properties);
	       
	    }

	    @Test
	    public void testGetByEnvironment_Success() {
	      
	        List<Property> properties = pr.getByEnvironment("dev");
	        assertNotNull(properties);
	       
	    }
	    @Test
	    public void testGetByEngine_NoMatch() {
	    
	        List<Property> properties = pr.getByEngine("None");
	        assertEquals(0, properties.size());
	    }

	    @Test
	    public void testGetByEnvironment_NoMatch() {
	      
	        List<Property> properties = pr.getByEnvironment("None");
	        assertEquals(0, properties.size());
	    }
	    
	    @Test
	    void testSave() { 
	       Property  prop1 = new Property(2L,app,"Innodb","dev",ptype,"abc","it is string");
	        pr.save(prop1);
	        Assertions.assertThat(prop1.getPropertyId()).isGreaterThan(0);
	    
	    }
	    @Test
	    public void testSaveAll_Success() {
	       
	        List<Property> applicationsToSave = new ArrayList<>();
	        applicationsToSave.add(prop);
	        pr.saveAll(applicationsToSave); 
	        List<Property> savedApplications =pr.findAll();
	        assertEquals(2, savedApplications.size());
	        Property savedApplication1 = savedApplications.get(0);
	        assertEquals("n1", savedApplication1.getPropertyName());
	       
	    }
	}



