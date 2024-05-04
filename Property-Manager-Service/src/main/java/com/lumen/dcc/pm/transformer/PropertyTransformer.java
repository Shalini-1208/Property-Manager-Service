package com.lumen.dcc.pm.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.dto.PropertyDTO;
import com.lumen.dcc.pm.dto.PropertyTypeDTO;
import com.lumen.dcc.pm.entity.Application;
import com.lumen.dcc.pm.entity.Property;
import com.lumen.dcc.pm.entity.PropertyType;

@Component
public class PropertyTransformer {
	
	@Autowired
	private ApplicationTransformer trans;
	@Autowired
	private PropertyTypeTransformer transform;

	public PropertyDTO transformToDto(Property app)
	{
		ApplicationDTO dto1=trans.transformToDto(app.getApplication());
		PropertyTypeDTO dto2=transform.transformToDto(app.getPropertyType());
		PropertyDTO app1=new PropertyDTO();
		app1.setPropertyId(app.getPropertyId());
		app1.setApplication(dto1);
		app1.setPropertyTypeId(dto2);
		app1.setEngine(app.getEngine());
		app1.setEnvironment(app.getEnvironment());
		app1.setPropertyName(app.getPropertyName());
		app1.setPropertyValue(app.getPropertyValue());
		return app1;
	}
	
	
	public List<PropertyDTO> transformToDto(List<Property>app)
	{
		return app.stream().map(this::transformToDto).collect(Collectors.toList());
	}
	
	
	public Property transformToEntity(PropertyDTO app)
	{
		Application dto1=trans.transformToEntity(app.getApplication());
		PropertyType dto2=transform.transformToEntity(app.getPropertyType());
		Property app1=new Property();
		app1.setPropertyId(app.getPropertyId());
		app1.setApplication(dto1);
		app1.setPropertyType(dto2);
		app1.setEngine(app.getEngine());
		app1.setEnvironment(app.getEnvironment());
		app1.setPropertyName(app.getPropertyName());
		app1.setPropertyValue(app.getPropertyValue());
		return app1;
	}
	
	public List<Property> transformToEntity(List<PropertyDTO>app)
	{
		return app.stream().map(this::transformToEntity).collect(Collectors.toList());
	}

}

