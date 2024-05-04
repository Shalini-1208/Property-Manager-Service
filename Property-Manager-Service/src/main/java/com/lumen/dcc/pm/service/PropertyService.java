package com.lumen.dcc.pm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.repository.PropertyRepository;
import com.lumen.dcc.pm.transformer.PropertyTransformer;

import jakarta.transaction.Transactional;

@Component
public class PropertyService {

	@Autowired
	private PropertyRepository repository;
	
	@Autowired
	private PropertyTransformer transformer;
	

	@Transactional
	public PropertyDTO createProperty(PropertyDTO app)
	{
		if(repository.existsById(app.getPropertyId()))
		{
			throw new RuntimeException("Property already exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(app)));
	}
	@Transactional
	public PropertyDTO getPropertyByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.getById(id));
	}
	@Transactional
	public List<PropertyDTO> getAll()
	{
		if(repository.findAll().isEmpty())
		{
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.findAll());
	}
	@Transactional
	public PropertyDTO UpdateProperty(Long id,PropertyDTO apps)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(apps)));
	}
	@Transactional
	public void removePropertyByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		repository.deleteById(id);
	}
	public List<PropertyDTO> getByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment)
	{
		if(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		return transformer.transformToDto(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
	}
	
	public List<PropertyDTO> getByEngineAndEnvironment(String engine, String environment) {
		if(repository.getByEngineAndEnvironment(engine, environment)==null)
		{
			throw new RuntimeException("data not exist");
		}
		
		return transformer.transformToDto(repository.getByEngineAndEnvironment(engine, environment));
	}
	
	public List<PropertyDTO>getByEngine(String engine)
	{
		if(repository.getByEngine(engine).isEmpty())
		{
			throw new RuntimeException("Property not exist");	
		}
		return transformer.transformToDto(repository.getByEngine(engine));	
	}
	
	public List<PropertyDTO>getByEnvironment(String environment)
	{
		if(repository.getByEnvironment(environment).isEmpty()) {
			
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.getByEnvironment(environment));	
	}
	
	public void deleteAllByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment)
	{
		if(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		repository.deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	}
	
}
