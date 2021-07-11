package com.ricardo.pmapp.persistence.repositories;

import com.ricardo.pmapp.persistence.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> getByName(String name);

    List<Project> findByProjectManager_Username(String username);

    List<Project> findByNameContainsIgnoreCase(String name);
}
