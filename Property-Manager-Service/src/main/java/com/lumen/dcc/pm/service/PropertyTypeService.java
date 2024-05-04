package com.lumen.dcc.pm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.repository.PropertyTypeRepository;
import com.lumen.dcc.pm.transformer.PropertyTypeTransformer;

import jakarta.transaction.Transactional;

@Component
public class PropertyTypeService {
	
	@Autowired
	private PropertyTypeRepository repository;
	
	@Autowired
	private PropertyTypeTransformer transformer;
	
	@Transactional
	public PropertyTypeDTO createPropertyType(PropertyTypeDTO app)
	{
		if(repository.existsById(app.getPropertyTypeId()))
		{
			throw new RuntimeException("id already exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(app)));
	}
	@Transactional
	public PropertyTypeDTO getPropertyTypeByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("id not exist");
		}
		return transformer.transformToDto(repository.getById(id));
	}
	
	@Transactional
	public List<PropertyTypeDTO> getAll()
	{
		if(repository.findAll().isEmpty())
		{
			throw new RuntimeException("PropertyType not exist");
		}
		return transformer.transformToDto(repository.findAll());
	}
	
	@Transactional
	public PropertyTypeDTO UpdatePropertyType(Long id,PropertyTypeDTO apps)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("PropertyType not exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(apps)));
	}
	@Transactional
	public void removePropertyTypeByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("PropertyType not exist");
		}
		repository.deleteById(id);
	}
	@Transactional
	public List<PropertyTypeDTO> getPropertyTypeByName(String name)
	{
		if(repository.getByName(name)==null)
		{
			throw new RuntimeException("PropertyType not exist");
		}
		return transformer.transformToDto(repository.getByName(name));
	}
	@Transactional
	public PropertyTypeDTO getByPropertyTypeIdAndName(Long id,String name)
	{
		if(repository.getByPropertyTypeIdAndName(id, name)==null)
		{
			throw new RuntimeException("PropertyType not exist");
		}
		return transformer.transformToDto(repository.getByPropertyTypeIdAndName(id, name));
		
	}

}
