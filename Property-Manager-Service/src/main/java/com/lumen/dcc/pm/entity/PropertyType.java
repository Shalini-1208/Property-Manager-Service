package com.lumen.dcc.pm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pm_property_type",schema="propmgrdb")
public class PropertyType {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROPERTY_TYPE_ID")
    private Long propertyTypeId;
	
    @Column(name="NAME")
    private String name;
    
    @Column(name="IMPLEMENTATION_CLASS")
    private String implementationClass;

    public PropertyType() {

	}

	public PropertyType(Long propertyTypeId, String implementationClass, String name) {
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
