package com.ricardo.pmapp.api.controllers;

import com.ricardo.pmapp.api.converters.ProjectConverter;
import com.ricardo.pmapp.api.models.dtos.ProjectDto;
import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.security.auth.CurrentUser;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.ProjectServiceI;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectServiceI projectService;

    private final ProjectConverter projectConverter;

    public ProjectController(ProjectServiceI projectService, ProjectConverter projectConverter) {
        this.projectService = projectService;
        this.projectConverter = projectConverter;
    }

    @RolesAllowed("Administrator")
    @PostMapping()
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) throws ProjectCreationException {
        return projectConverter.ToDto(projectService.create(projectConverter.ToEntity(projectDto)));
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @GetMapping("/{code}")
    public ProjectDto getProjectByCode(@PathVariable Long code, @CurrentUser UserPrincipal userPrincipal)
            throws ProjectNotFoundException, AccessDeniedException {
        return projectConverter.ToDto(projectService.getByCode(code, userPrincipal));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/getByName/{name}")
    public ProjectDto getProjectByName(@PathVariable String name) throws ProjectNotFoundException {
        return projectConverter.ToDto(projectService.getByName(name));
    }

    @GetMapping("/all")
    public List<ProjectDto> findAllProjects() {
        return projectService.findAll().stream().map(projectConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @GetMapping("/me")
    public List<ProjectDto> findCurrentUserProjects(@CurrentUser UserPrincipal userPrincipal) {
        return projectService.findByProjectManager(userPrincipal.getUsername()).stream()
                .map(projectConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed("Administrator")
    @GetMapping("/findByProjectManager/{username}")
    public List<ProjectDto> findProjectsByProjectManager(@PathVariable String username) {
        return projectService.findByProjectManager(username).stream()
                .map(projectConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed("Administrator")
    @GetMapping("/findByName/{name}")
    public List<ProjectDto> findProjectsByName(@PathVariable String name) {
        return projectService.findByName(name).stream()
                .map(projectConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @PutMapping("/{code}")
    public ProjectDto updateProjectByCode(@RequestBody ProjectDto projectDto, @PathVariable Long code,
                                          @CurrentUser UserPrincipal userPrincipal)
            throws ProjectNotFoundException, ProjectUpdateException, AccessDeniedException {
        projectDto.setCode(code);
        return projectConverter.ToDto(projectService.update(projectConverter.ToEntity(projectDto), userPrincipal));
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @DeleteMapping("/{code}")
    public void deleteProjectByCode(@PathVariable Long code, @CurrentUser UserPrincipal userPrincipal)
            throws ProjectNotFoundException, ProjectDeletionException, AccessDeniedException {
        projectService.deleteByCode(code, userPrincipal);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping("/deleteByProjectManager/{username}")
    public void deleteProjectsByProjectManager(@PathVariable String username) throws ProjectDeletionException {
        projectService.deleteByProjectManager(username);
    }
}
