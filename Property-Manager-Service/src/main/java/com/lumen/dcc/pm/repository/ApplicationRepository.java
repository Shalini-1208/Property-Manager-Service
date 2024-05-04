package com.lumen.dcc.pm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumen.dcc.pm.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> getByName(String name);
    void deleteByName(String name);
	Application getById(Long id);
	Optional<Application> findByName(String name);   

} 