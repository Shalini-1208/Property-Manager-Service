package com.lumen.dcc.pm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.repository.ApplicationRepository;
import com.lumen.dcc.pm.transformer.ApplicationTransformer;

import jakarta.transaction.Transactional;

@Component
public class ApplicationService {
	
	@Autowired
	private ApplicationRepository repository;
	
	@Autowired
	private ApplicationTransformer transformer;
	
	@Transactional
	@CachePut(value = "application", key = "#result.id")
	public ApplicationDTO createApplication(ApplicationDTO app)
	{
		if(repository.existsById(app.getApplicationId()))
		{
			throw new RuntimeException("id already exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(app)));
	}
	@Transactional
	@Cacheable(value = "application", key = "#id")
	public ApplicationDTO getApplicationByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Application not exist");
		}
		return transformer.transformToDto(repository.getById(id));
	}
	@Transactional
	@CacheEvict(value = {"applications"}, key = "#id")
	public void removeApplicationByID(Long id)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("Application not exist");
		}
	
		repository.deleteById(id);
	}
	@Transactional
    @Cacheable(value = "applications", key = "#name")
	public List<ApplicationDTO> getApplicationByName(String name)
	{
	
		if(repository.getByName(name).isEmpty())
		{
			throw new RuntimeException("Application not exist");
		}
		return transformer.transformToDto(repository.getByName(name));
		
	}
	@Transactional
	@CacheEvict(value = {"applications"}, key = "#name")
	public void removeApplicationByName(String name)
	{
		if(repository.getByName(name).isEmpty())
		{
			throw new RuntimeException("Application not exist");
		}
		repository.deleteByName(name);
	}
	@Transactional
	@Cacheable(value = "applications")
	public List<ApplicationDTO> getAll()
	{
		if(repository.findAll().isEmpty())
		{
			throw new RuntimeException("Application not exist");
		}
			
		return transformer.transformToDto(repository.findAll());
	}
	@Transactional
	@CachePut(value = "application", key = "#id")
	public ApplicationDTO UpdateApplication(Long id,ApplicationDTO apps)
	{
		if(repository.findById(id)==null)
		{
			throw new RuntimeException("id not exist");
		}
		return transformer.transformToDto(repository.save(transformer.transformToEntity(apps)));
	}
	
	
}

