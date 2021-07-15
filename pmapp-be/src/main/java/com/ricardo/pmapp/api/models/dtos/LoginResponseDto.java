package com.ricardo.pmapp.api.models.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an User Login Response Dto
 */

@Getter
@Setter
public class LoginResponseDto {

    private String accessToken;

    private String tokenType = "Bearer";

    private UserDto user;

    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponseDto(String accessToken, UserDto userDto) {
        this.accessToken = accessToken;
        this.user = userDto;
    }
}
