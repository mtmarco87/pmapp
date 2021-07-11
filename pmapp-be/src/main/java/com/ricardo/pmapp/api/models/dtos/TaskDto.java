package com.ricardo.pmapp.api.models.dtos;

import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    private Long project;

    private String description;

    @Min(0)
    @Max(100)
    private int progress;

    private TaskStatus status;

    private Date deadline;
}
