package com.ricardo.pmapp.services;

import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Interface providing basics CRUD operation on a {@link Task}
 */

public interface TaskServiceI {

    Task create(Task task, UserPrincipal requester) throws TaskCreationException, AccessDeniedException;

    Task getByCode(Long code) throws TaskNotFoundException;

    List<Task> findAll();

    List<Task> findByAssignee(String username);

    List<Task> findNotAssigned();

    List<Task> findByProject(Long code);

    Task update(Task task, UserPrincipal requester) throws TaskNotFoundException, TaskUpdateException, AccessDeniedException;

    void deleteByCode(Long code, UserPrincipal requester) throws TaskNotFoundException, TaskDeletionException, AccessDeniedException;

    void deleteByAssignee(String username) throws TaskDeletionException;

    void deleteByProject(Long code, UserPrincipal requester) throws TaskDeletionException, AccessDeniedException;
}
