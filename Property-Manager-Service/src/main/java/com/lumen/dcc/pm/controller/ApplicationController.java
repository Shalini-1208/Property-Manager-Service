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

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
public class ApplicationController {
	
	@Autowired
	private ApplicationService service;
	
	
	@PostMapping("/Applications")
	public ResponseEntity<ApplicationDTO> createApplication(@Valid @RequestBody ApplicationDTO app)
	{
		return new ResponseEntity<ApplicationDTO>(service.createApplication(app),HttpStatus.CREATED);
	}
	
	@PutMapping("/Applications/{id}")
	public ResponseEntity<ApplicationDTO>updateApplication( @PathVariable Long id,@Valid @RequestBody ApplicationDTO app)
	{
		return new ResponseEntity<ApplicationDTO>(service.UpdateApplication(id, app),HttpStatus.OK);
	}
	@GetMapping("/Applications/{id}")
	public  ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable("id") Long id)
	{
		return new ResponseEntity<ApplicationDTO>(service.getApplicationByID(id),HttpStatus.OK);
	}
	@GetMapping("/Applications")
	public ResponseEntity<List<ApplicationDTO>> getAllApplications()
	{
		return new ResponseEntity<List<ApplicationDTO>>(service.getAll(),HttpStatus.OK);
	}
	@GetMapping("/Applications/name/{name}")
	public ResponseEntity<List<ApplicationDTO>> getApplicationByName(@PathVariable("name") String name)
	{
		return new ResponseEntity<List<ApplicationDTO>>(service.getApplicationByName(name),HttpStatus.OK);
	}
	
	@DeleteMapping("/Applications/{id}")
	public void delteApplicationById(@PathVariable("id") Long id)
	{
		service.removeApplicationByID(id);	
	}
	@DeleteMapping("/Applications/name/{name}")
	public void delteApplicationByName(@PathVariable("name") String name)
	{
		service.removeApplicationByName(name);
	}	

}
