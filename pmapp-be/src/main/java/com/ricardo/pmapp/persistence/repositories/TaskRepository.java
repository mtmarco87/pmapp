package com.ricardo.pmapp.persistence.repositories;

import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignee_Username(String username);

    List<Task> findByStatus(TaskStatus status);

    void deleteByAssignee_Username(String username);
}
