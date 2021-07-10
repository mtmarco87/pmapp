package com.ricardo.pmapp.api.models.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents an User Login Request Dto
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequestDto implements Serializable {

    @NotNull
    @ApiModelProperty(required = true)
    private String username;

    @NotNull
    @ApiModelProperty(required = true)
    private String password;
}
