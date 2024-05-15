package com.lumen.dcc.pm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
	@CachePut(value = "property", key = "#result.id")
	public PropertyDTO createProperty(PropertyDTO app)
	{
		if(repository.existsById(app.getPropertyId()))
		{
			throw new RuntimeException("Property already exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(app)));
	}
	@Transactional
	@Cacheable(value = "property", key = "#id")
	public PropertyDTO getPropertyByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.getById(id));
	}
	@Transactional
	@Cacheable(value = "properties")
	public List<PropertyDTO> getAll()
	{
		if(repository.findAll().isEmpty())
		{
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.findAll());
	}
	@Transactional
	@CachePut(value = "property", key = "#id")
	public PropertyDTO UpdateProperty(Long id,PropertyDTO apps)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(apps)));
	}
	@Transactional
	@CacheEvict(value = {"properties"}, key = "#id")
	public void removePropertyByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		repository.deleteById(id);
	}
	
	@Transactional
	@Cacheable(value = "properties", key = "#name.concat('-').concat(#engine).concat('-').concat(#environment)")
	public List<PropertyDTO> getByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment)
	{
		if(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		return transformer.transformToDto(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment));
	}
	@Transactional
	@Cacheable(value = "properties", key = "#engine.concat('-').concat(#environment)")
	public List<PropertyDTO> getByEngineAndEnvironment(String engine, String environment) {
		if(repository.getByEngineAndEnvironment(engine, environment)==null)
		{
			throw new RuntimeException("data not exist");
		}
		
		return transformer.transformToDto(repository.getByEngineAndEnvironment(engine, environment));
	}
	@Transactional
	@Cacheable(value = "properties", key = "#engine")
	public List<PropertyDTO>getByEngine(String engine)
	{
		if(repository.getByEngine(engine).isEmpty())
		{
			throw new RuntimeException("Property not exist");	
		}
		return transformer.transformToDto(repository.getByEngine(engine));	
	}
	@Transactional
	@Cacheable(value = "properties", key = "#environment")
	public List<PropertyDTO>getByEnvironment(String environment)
	{
		if(repository.getByEnvironment(environment).isEmpty()) {
			
			throw new RuntimeException("Property not exist");
		}
		return transformer.transformToDto(repository.getByEnvironment(environment));	
	}
	@Transactional
	@CacheEvict(value = "properties", allEntries = true)
	public void deleteAllByPropertyNameAndEngineAndEnvironment(String propertyName, String engine, String environment)
	{
		if(repository.getByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment)==null)
		{
			throw new RuntimeException("Property not exist");
			
		}
		repository.deleteAllByPropertyNameAndEngineAndEnvironment(propertyName, engine, environment);
	}
	
}
