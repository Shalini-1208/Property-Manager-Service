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

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.service.PropertyTypeService;

import jakarta.validation.Valid;

@RestController
public class PropertyTypeController {
	@Autowired
	private PropertyTypeService service;
	
	
	@PostMapping("/PropertyTypes")
	public ResponseEntity<PropertyTypeDTO> createPropertyType(@Valid @RequestBody PropertyTypeDTO app)
	{
		return new ResponseEntity<PropertyTypeDTO>(service.createPropertyType(app),HttpStatus.CREATED);
	}
	@PutMapping("/PropertyTypes/{id}")
	public ResponseEntity<PropertyTypeDTO> updatePropertyType( @PathVariable Long id,@Valid @RequestBody PropertyTypeDTO app)
	{
		return new ResponseEntity<PropertyTypeDTO>(service.UpdatePropertyType(id, app),HttpStatus.OK);
	}
	@GetMapping("/PropertyTypes/{id}")
	public ResponseEntity<PropertyTypeDTO> getPropertyTypeById(@PathVariable("id") Long id)
	{
		return new ResponseEntity<PropertyTypeDTO>(service.getPropertyTypeByID(id),HttpStatus.OK);
	} 
	@GetMapping("/PropertyTypes")
	public ResponseEntity<List<PropertyTypeDTO>> getAllPropertyTypes()
	{
		return new ResponseEntity<List<PropertyTypeDTO>>(service.getAll(),HttpStatus.OK);
	}
	@DeleteMapping("/PropertyTypes/{id}")
	public void deletePropertyById(@PathVariable("id") Long id)
	{
		service.removePropertyTypeByID(id);
	}
	@GetMapping("/PropertyTypes/{id}/{name}")
	public ResponseEntity<PropertyTypeDTO> getByPropertyTypeIdAndName(@PathVariable Long id,@PathVariable String name)
	{
		return new ResponseEntity<PropertyTypeDTO>(service.getByPropertyTypeIdAndName(id, name),HttpStatus.OK);
	} 
	@GetMapping("/PropertyTypes/name/{name}")
	public ResponseEntity<List<PropertyTypeDTO>> getPropertyTypeByName(@PathVariable("name") String name)
	{
		return new ResponseEntity<List<PropertyTypeDTO>>(service.getPropertyTypeByName(name),HttpStatus.OK);
	} 

	
}
