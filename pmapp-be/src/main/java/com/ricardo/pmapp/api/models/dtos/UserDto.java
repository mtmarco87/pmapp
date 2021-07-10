package com.ricardo.pmapp.api.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ricardo.pmapp.persistence.models.enums.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * This class represents an User Dto
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto implements Serializable {

    @NotNull
    @ApiModelProperty(required = true)
    private String username;

    @Email
    @NotNull
    @ApiModelProperty(required = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private String surname;
    
    private Role role;
}
