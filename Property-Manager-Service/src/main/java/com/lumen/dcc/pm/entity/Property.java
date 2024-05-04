package com.lumen.dcc.pm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name="pm_property",schema="propmgrdb")
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROPERTY_ID")
    private Long propertyId;
    
    @OneToOne
    @JoinColumn(name="APPLICATION_ID",referencedColumnName="APPLICATION_ID")
    private Application application;
    
    @Column(name="ENGINE")
    private String engine;
    
    @Column(name="ENVIRONMENT")
    private String environment;
    
    @OneToOne
    @JoinColumn(name="PROPERTY_TYPE_ID" ,referencedColumnName="PROPERTY_TYPE_ID")
    private PropertyType propertyType;
    
    @Column(name="PROPERTY_NAME")
    private String propertyName;
    
    @Column(name="PROPERTY_VALUE")
    private String propertyValue;	
    
	public Property() {
	}

	public Property(Long propertyId, Application applicationId, String engine, String environment,
			PropertyType propertyType, String propertyName, String propertyValue) {
		super();
		this.propertyId = propertyId;
		this.application = applicationId;
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

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
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

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
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

