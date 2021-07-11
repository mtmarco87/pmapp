package com.ricardo.pmapp.api.converters;

import com.ricardo.pmapp.api.models.dtos.ProjectDto;
import com.ricardo.pmapp.persistence.models.entities.Project;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ProjectConverter {

    private final ModelMapper modelMapper;

    public ProjectConverter(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
        PropertyMap<ProjectDto, Project> projectMap = new PropertyMap <ProjectDto, Project>() {
            protected void configure() {
                map().getProjectManager().setUsername(source.getProjectManager());
            }
        };
        this.modelMapper.addMappings(projectMap);
        this.modelMapper.typeMap(Project.class, ProjectDto.class)
                .addMappings(m -> m.map(src -> src.getProjectManager().getUsername(), ProjectDto::setProjectManager));
    }

    public Project ToEntity(ProjectDto projectDto) {
        if (projectDto != null) {
            return modelMapper.map(projectDto, Project.class);
        } else {
            return null;
        }
    }

    public ProjectDto ToDto(Project project) {
        if (project != null) {
            return modelMapper.map(project, ProjectDto.class);
        } else {
            return null;
        }
    }
}
