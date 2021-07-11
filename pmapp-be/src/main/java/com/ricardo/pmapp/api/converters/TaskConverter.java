package com.ricardo.pmapp.api.converters;

import com.ricardo.pmapp.api.models.dtos.TaskDto;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.entities.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class TaskConverter {

    private final ModelMapper modelMapper;

    public TaskConverter(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
        PropertyMap<TaskDto, Task> taskMap = new PropertyMap <TaskDto, Task>() {
            protected void configure() {
                map().getAssignee().setUsername(source.getAssignee());
                map().getProject().setCode(source.getProject());
            }
        };
        this.modelMapper.addMappings(taskMap);
        this.modelMapper.typeMap(Task.class, TaskDto.class)
                .addMappings(m -> m.map(src -> src.getAssignee().getUsername(), TaskDto::setAssignee))
                .addMappings(m -> m.map(src -> src.getProject().getCode(), TaskDto::setProject));
    }

    public Task ToEntity(TaskDto taskDto) {
        if (taskDto != null) {
            return modelMapper.map(taskDto, Task.class);
        } else {
            return null;
        }
    }

    public TaskDto ToDto(Task task) {
        if (task != null) {
            return modelMapper.map(task, TaskDto.class);
        } else {
            return null;
        }
    }
}
