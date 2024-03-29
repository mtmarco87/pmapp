package com.ricardo.pmapp.api.controllers;

import com.ricardo.pmapp.api.converters.TaskConverter;
import com.ricardo.pmapp.api.models.dtos.TaskDto;
import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.security.auth.CurrentUser;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.TaskServiceI;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskServiceI taskService;

    private final TaskConverter taskConverter;

    public TaskController(TaskServiceI taskService, TaskConverter taskConverter) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @PostMapping()
    public TaskDto createTask(@RequestBody TaskDto taskDto, @CurrentUser UserPrincipal userPrincipal)
            throws TaskCreationException, AccessDeniedException {
        return taskConverter.ToDto(taskService.create(taskConverter.ToEntity(taskDto), userPrincipal));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/{code}")
    public TaskDto getTaskByCode(@PathVariable Long code) throws TaskNotFoundException {
        return taskConverter.ToDto(taskService.getByCode(code));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/all")
    public List<TaskDto> findAllTasks() {
        return taskService.findAll().stream().map(taskConverter::ToDto).collect(Collectors.toList());
    }

    @GetMapping("/me")
    public List<TaskDto> findCurrentUserTasks(@CurrentUser UserPrincipal userPrincipal) {
        return taskService.findByAssignee(userPrincipal.getUsername()).stream().map(taskConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed("Administrator")
    @GetMapping("/findByAssignee/{username}")
    public List<TaskDto> findTasksByAssignee(@PathVariable String username) {
        return taskService.findByAssignee(username).stream().map(taskConverter::ToDto).collect(Collectors.toList());
    }

    @GetMapping("/findNotAssigned")
    public List<TaskDto> findNotAssignedTasks() {
        return taskService.findNotAssigned().stream().map(taskConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @GetMapping("/findByProject/{code}")
    public List<TaskDto> findTasksByProject(@PathVariable Long code) {
        return taskService.findByProject(code).stream().map(taskConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed({"Administrator", "ProjectManager", "Developer"})
    @PutMapping("/{code}")
    public TaskDto updateTaskByCode(@RequestBody TaskDto taskDto, @PathVariable Long code,
                                    @CurrentUser UserPrincipal userPrincipal)
            throws TaskNotFoundException, TaskUpdateException, AccessDeniedException {
        taskDto.setCode(code);
        return taskConverter.ToDto(taskService.update(taskConverter.ToEntity(taskDto), userPrincipal));
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @DeleteMapping("/{code}")
    public void deleteTaskByCode(@PathVariable Long code, @CurrentUser UserPrincipal userPrincipal)
            throws TaskNotFoundException, TaskDeletionException, AccessDeniedException {
        taskService.deleteByCode(code, userPrincipal);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping("/deleteByAssignee/{username}")
    public void deleteTasksByAssignee(@PathVariable String username) throws TaskDeletionException {
        taskService.deleteByAssignee(username);
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @DeleteMapping("/deleteByProject/{code}")
    public void deleteTasksByProject(@PathVariable Long code, @CurrentUser UserPrincipal userPrincipal)
            throws TaskDeletionException, AccessDeniedException {
        taskService.deleteByProject(code, userPrincipal);
    }
}
