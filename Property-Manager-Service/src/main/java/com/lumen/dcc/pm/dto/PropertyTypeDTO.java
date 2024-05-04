package com.lumen.dcc.pm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyTypeDTO {

	private Long propertyTypeId;
	
	@NotBlank(message="PropertyType name is required")
    private String name;
	
	@NotBlank(message="PropertyType class is required")
    private String implementationClass;

    public PropertyTypeDTO() {
    }

    public PropertyTypeDTO(Long propertyTypeId, String name, String implementationClass) {
        this.propertyTypeId = propertyTypeId;
        this.name = name;
        this.implementationClass = implementationClass;
    }

    public Long getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }
}

