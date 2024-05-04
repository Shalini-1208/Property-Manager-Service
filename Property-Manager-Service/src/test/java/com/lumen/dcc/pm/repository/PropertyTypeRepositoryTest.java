package com.lumen.dcc.pm.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lumen.dcc.pm.entity.PropertyType;

@RunWith(SpringRunner.class)
@SpringBootTest
class PropertyTypeRepositoryTest {
	
	    @Autowired
	    private PropertyTypeRepository pm;

	    private PropertyType ptype;
	    
	    @BeforeEach
	    void setUp() {
	        ptype = new PropertyType(1L,"it is name","abc");
	        pm.save(ptype);
	    }
		
		@Test
	    public void testGetByName_Success() {
	       
	        List<PropertyType> propertyTypes = pm.getByName("abc");
	        assertNotNull(propertyTypes);
	        assertEquals("abc", propertyTypes.get(0).getName());
	    }

	    @Test
	    public void testGetByName_NoMatch() {
	        
	        List<PropertyType> propertyTypes = pm.getByName("None");
	        assertNotNull(propertyTypes);
	        assertEquals(0, propertyTypes.size());
	    }
	    @Test
	    public void testGetByPropertyTypeIdAndName_Success() {
	        
	        Optional<PropertyType> retrievedPropertyType = Optional.ofNullable(pm.getByPropertyTypeIdAndName(1L, "abc"));

	        assertTrue(retrievedPropertyType.isPresent());
	        assertEquals(1L, retrievedPropertyType.get().getPropertyTypeId());
	        assertEquals("abc", retrievedPropertyType.get().getName());
	    }

	    @Test
	    public void testGetByPropertyTypeIdAndName_NoMatch() {
	       
	        Optional<PropertyType> retrievedPropertyType = Optional.ofNullable(pm.getByPropertyTypeIdAndName(1L, "None"));
	        assertFalse(retrievedPropertyType.isPresent());
	    }
	    
	    @Test
	    void testSave() { 
	        PropertyType  prop1 = new PropertyType(2L,"it is name","abcd");
	        PropertyType  prop2=pm.save(prop1);
	        assertNotNull(prop2);
	    }
	    @Test
	    public void testSaveAll_Success() {
	       
	        List<PropertyType> applicationsToSave = new ArrayList<>();
	        applicationsToSave.add(ptype);
	        pm.saveAll(applicationsToSave); 
	        List<PropertyType> savedApplications =pm.findAll();
	        PropertyType savedApplication1 = savedApplications.get(0);
	        assertEquals("abc", savedApplication1.getName());
	       
	    }
}




