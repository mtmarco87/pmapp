package com.ricardo.pmapp.api.models.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents a Project Dto
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectDto implements Serializable {

    private Long code;

    private String name;

    @NotNull
    @ApiModelProperty(required = true)
    private String projectManager;
}
