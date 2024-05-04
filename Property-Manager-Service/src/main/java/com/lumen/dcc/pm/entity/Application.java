package com.lumen.dcc.pm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name="pm_application",schema="propmgrdb")
@Builder
public class Application {

   

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="APPLICATION_ID")
    private Long applicationId;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="DESCRIPTION")
    private String description;

    public Application(Long applicationId, String name, String description) {
		super();
		this.applicationId = applicationId;
		this.name = name;
		this.description = description;
	}
    
	public Application() {
		
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

 
}

