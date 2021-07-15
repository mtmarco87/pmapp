package com.ricardo.pmapp.services;

import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.persistence.models.entities.Project;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;


/**
 * Interface providing basics CRUD operation on a {@link Project}
 */

public interface ProjectServiceI {

    Project create(Project task) throws ProjectCreationException;

    Project getByCode(Long code, UserPrincipal requester) throws ProjectNotFoundException, AccessDeniedException;

    Project getByName(String name) throws ProjectNotFoundException;

    List<Project> findAll();

    List<Project> findByProjectManager(String username);

    List<Project> findByName(String name);

    Project update(Project project, UserPrincipal requester) throws ProjectNotFoundException, ProjectUpdateException, AccessDeniedException;

    void deleteByCode(Long code, UserPrincipal requester) throws ProjectNotFoundException, ProjectDeletionException, AccessDeniedException;

    void deleteByProjectManager(String username) throws ProjectDeletionException;
}
