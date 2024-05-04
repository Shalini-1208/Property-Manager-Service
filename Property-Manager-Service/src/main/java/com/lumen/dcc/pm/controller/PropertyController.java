package com.lumen.dcc.pm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.service.PropertyService;

import jakarta.validation.Valid;

@RestController
public class PropertyController {
	
	@Autowired
	private PropertyService service;
	
	
	@PostMapping("/Properties")
	public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyDTO app)
	{
		return new ResponseEntity<PropertyDTO>(service.createProperty(app),HttpStatus.CREATED);
	}
	@PutMapping("/Properties/{id}")
	public ResponseEntity<PropertyDTO> updateProperty( @PathVariable Long id,@Valid @RequestBody PropertyDTO app)
	{
		return new ResponseEntity<PropertyDTO>(service.UpdateProperty(id, app),HttpStatus.OK);
	}
	@GetMapping("/Properties/{id}")
	public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable("id") Long id)
	{
		return new ResponseEntity<PropertyDTO>(service.getPropertyByID(id),HttpStatus.OK);
	} 
	@GetMapping("/Properties")
	public ResponseEntity<List<PropertyDTO>> getAllProperties()
	{
		return new ResponseEntity<List<PropertyDTO>>(service.getAll(),HttpStatus.OK);
	}
	@DeleteMapping("/Properties/{id}")
	public void deletePropertyById(@PathVariable("id") Long id)
	{
		service.removePropertyByID(id);
	}
	@GetMapping("/Properties/engine/{engine}")
	public ResponseEntity<List<PropertyDTO>> getPropertyByEngine(@PathVariable("engine") String engine)
	{
		return new ResponseEntity<List<PropertyDTO>>(service.getByEngine(engine),HttpStatus.OK);
	}
	@GetMapping("/Properties/environment/{environment}")
	public ResponseEntity<List<PropertyDTO>> getPropertyByEnvironment(@PathVariable("environment") String environment)
	{
		return new ResponseEntity<List<PropertyDTO>>(service.getByEnvironment(environment),HttpStatus.OK);
	}
	@GetMapping("/Properties/{engine}/{environment}")
	public ResponseEntity<List<PropertyDTO>> getPropertyByEngineAndEnvironment(@PathVariable String engine,@PathVariable String environment)
	{
		return new ResponseEntity<List<PropertyDTO>>(service.getByEngineAndEnvironment(engine, environment),HttpStatus.OK);
	} 
	@GetMapping("/Properties/{name}/{engine}/{environment}")
	public ResponseEntity<List<PropertyDTO>> getPropertyByNameAndEngineAndEnvironment(@PathVariable String name,@PathVariable String engine,@PathVariable String environment)
	{
		return new ResponseEntity<List<PropertyDTO>>(service.getByPropertyNameAndEngineAndEnvironment(name, engine, environment),HttpStatus.OK);
	} 
	@DeleteMapping("/Properties/{name}/{engine}/{environment}")
	public void deltePropertyByNameAndEngineAndEnvironment(@PathVariable String name,@PathVariable String engine,@PathVariable String environment)
	{
		service.deleteAllByPropertyNameAndEngineAndEnvironment(name, engine, environment);
	}
	

}

