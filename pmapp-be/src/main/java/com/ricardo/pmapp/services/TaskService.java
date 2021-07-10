package com.ricardo.pmapp.services;

import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.repositories.TaskRepository;
import com.ricardo.pmapp.persistence.repositories.UserRepository;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TaskService implements TaskServiceI {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Task create(Task task, UserPrincipal requester) throws TaskCreationException {
        // TODO verify that PM owns task

        if (task.getCode() != null) {
            throw new TaskCreationException(ExceptionMessages.TASK_CODE_IN_CREATION);
        } else if (task.getAssignee() != null && task.getAssignee().getUsername() != null &&
                !userRepository.findById(task.getAssignee().getUsername()).isPresent()) {
            throw new TaskCreationException(String.format(ExceptionMessages.ASSIGNEE_NOT_EXISTING,
                    task.getAssignee().getUsername()));
        }

        return taskRepository.save(task);
    }

    @Override
    public Task getByCode(Long code) throws TaskNotFoundException {
        return taskRepository.findById(code)
                .orElseThrow(() -> new TaskNotFoundException(String.format(ExceptionMessages.TASK_NOT_EXISTING,
                        code)));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByAssignee(String username) {
        return taskRepository.findByAssignee_Username(username);
    }

    @Override
    public List<Task> findNotAssigned() {
        return findByAssignee(null);
    }

    @Override
    @Transactional
    public Task update(Task task, UserPrincipal requester) throws TaskNotFoundException, TaskUpdateException {
        // TODO verify that PM owns task

        // Check that provided Task exists first
        getByCode(task.getCode());

        if (task.getAssignee() != null && task.getAssignee().getUsername() != null &&
                !userRepository.findById(task.getAssignee().getUsername()).isPresent()) {
            throw new TaskUpdateException(String.format(ExceptionMessages.ASSIGNEE_NOT_EXISTING,
                    task.getAssignee().getUsername()));
        }

        if (task.getAssignee() != null) {
            task.setAssignee(userRepository.getById(task.getAssignee().getUsername()));
        }

        return taskRepository.save(task);
    }

    @Override
    public void unassignByAssignee(String username, UserPrincipal requester) {
        // TODO verify that PM owns tasks

        List<Task> tasksToUnassign = findByAssignee(username);
        tasksToUnassign.forEach(t -> {
            t.setAssignee(null);
            taskRepository.save(t);
        });
    }

    @Override
    public void deleteByCode(Long code, UserPrincipal requester) throws TaskNotFoundException {
        // TODO verify that PM owns task

        try{
            taskRepository.deleteById(code);
        } catch (Exception ex){
            throw new TaskNotFoundException(String.format(ExceptionMessages.TASK_NOT_EXISTING, code));
        }
    }

    @Transactional
    @Override
    public void deleteByAssignee(String username, UserPrincipal requester) throws TaskDeletionException {
        // TODO verify that PM owns task

        try {
            taskRepository.deleteByAssignee_Username(username);
        } catch (Exception ex) {
            throw new TaskDeletionException(ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
    }
}
