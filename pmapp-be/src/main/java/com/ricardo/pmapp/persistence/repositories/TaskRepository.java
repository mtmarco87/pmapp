package com.ricardo.pmapp.persistence.repositories;

import com.ricardo.pmapp.persistence.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignee_Username(String username);

    List<Task> findByProject_Code(Long code);

    void deleteByAssignee_Username(String username);

    void deleteByProject_Code(Long code);
}
