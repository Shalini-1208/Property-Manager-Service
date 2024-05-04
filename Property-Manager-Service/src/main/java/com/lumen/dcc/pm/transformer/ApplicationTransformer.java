package com.lumen.dcc.pm.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lumen.dcc.pm.dto.ApplicationDTO;
import com.lumen.dcc.pm.entity.Application;

@Component
public class ApplicationTransformer {

	public ApplicationDTO transformToDto(Application app)
	{
		ApplicationDTO app1=new ApplicationDTO();
		app1.setApplicationId(app.getApplicationId());
		app1.setName(app.getName());
		app1.setDescription(app.getDescription());
		return app1;
	}

	public List<ApplicationDTO> transformToDto(List<Application>app)
	{
		return app.stream().map(this::transformToDto).collect(Collectors.toList());
	}
	
	public Application transformToEntity(ApplicationDTO app)
	{
		Application app1=new Application();
		app1.setApplicationId(app.getApplicationId());
		app1.setName(app.getName());
		app1.setDescription(app.getDescription());
		return app1;
	}
	
	public List<Application> transformToEntity(List<ApplicationDTO>app)
	{
		return app.stream().map(this::transformToEntity).collect(Collectors.toList());
	}

}
