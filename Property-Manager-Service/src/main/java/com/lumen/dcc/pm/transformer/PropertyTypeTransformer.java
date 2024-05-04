package com.lumen.dcc.pm.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.PropertyType;

@Component
public class PropertyTypeTransformer {

	public PropertyTypeDTO transformToDto(PropertyType app)
	{
		PropertyTypeDTO app1=new PropertyTypeDTO();
		app1.setPropertyTypeId(app.getPropertyTypeId());
		app1.setName(app.getName());
		app1.setImplementationClass(app.getImplementationClass());
		return app1;
	}
	
	
	public List<PropertyTypeDTO> transformToDto(List<PropertyType>app)
	{
		return app.stream().map(this::transformToDto).collect(Collectors.toList());
	}
	
	
	public PropertyType transformToEntity(PropertyTypeDTO apps)
	{
		PropertyType app1=new PropertyType();
		app1.setPropertyTypeId(apps.getPropertyTypeId());
		app1.setName(apps.getName());
		app1.setImplementationClass(apps.getImplementationClass());
		return app1;
	}
	
	
	public List<PropertyType> transformToEntity(List<PropertyTypeDTO>app)
	{
		return app.stream().map(this::transformToEntity).collect(Collectors.toList());
	}
	

}

