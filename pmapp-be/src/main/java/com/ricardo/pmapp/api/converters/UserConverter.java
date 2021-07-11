package com.ricardo.pmapp.api.converters;

import com.ricardo.pmapp.api.models.dtos.UserDto;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    private final ModelMapper modelMapper;

    public UserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User ToEntity(UserDto userDto) {
        if (userDto != null) {
            return modelMapper.map(userDto, User.class);
        } else {
            return null;
        }
    }

    public UserDto ToDto(User user) {
        if (user != null) {
            return modelMapper.map(user, UserDto.class);
        } else {
            return null;
        }
    }

    public UserDto ToDto(UserPrincipal userPrincipal) {
        if (userPrincipal != null) {
            return modelMapper.map(userPrincipal, UserDto.class);
        } else {
            return null;
        }
    }
}
