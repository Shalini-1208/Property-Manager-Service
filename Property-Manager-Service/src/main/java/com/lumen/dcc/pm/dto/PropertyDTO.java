package com.lumen.dcc.pm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyDTO {

	
	private Long propertyId;
	
	@NotNull(message="Property id should not be null")
    private ApplicationDTO application;
	
    @NotBlank(message="Property engine is required")
    private String engine;
    
    @NotBlank(message="Property environment is required")
    private String environment;
    
    @NotNull(message="Property id should not be null")
    private PropertyTypeDTO propertyType;
    
    @NotBlank(message="Property name is required")
    private String propertyName;
    
    @NotBlank(message="Property value is required")
    private String propertyValue; 
    
    public PropertyDTO() {
    }

    public PropertyDTO(Long propertyId, ApplicationDTO application, String engine, String environment, PropertyTypeDTO propertyType, String propertyName, String propertyValue) {
        this.propertyId = propertyId;
        this.application = application;
        this.engine = engine;
        this.environment = environment;
        this.propertyType = propertyType;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public PropertyTypeDTO getPropertyType() {
        return propertyType;
    }

    public void setPropertyTypeId(PropertyTypeDTO propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }


}


