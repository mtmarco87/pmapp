package com.ricardo.pmapp.api.converters;

import com.ricardo.pmapp.PmAppApplication;
import com.ricardo.pmapp.api.models.dtos.UserDto;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PmAppApplication.class)
public class UserConverterTest {

    @Autowired
    private UserConverter userConverter;

    @Test
    public void shouldConvertToDto() {
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        UserDto userDto = userConverter.ToDto(user);
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getSurname(), userDto.getSurname());
        assertEquals(user.getRole(), userDto.getRole());
    }

    @Test
    public void shouldConvertToEntity() {
        UserDto userDto = new UserDto("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer);
        User user = userConverter.ToEntity(userDto);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getSurname(), user.getSurname());
        assertEquals(userDto.getRole(), user.getRole());
    }
}
