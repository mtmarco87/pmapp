package com.ricardo.pmapp.api.models.dtos;

import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a Task Dto
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDto implements Serializable {

    private Long code;

    private String assignee;

    private String description;

    private int progress;

    private TaskStatus status;

    private Date deadline;
}
