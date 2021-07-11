package com.ricardo.pmapp.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardo.pmapp.api.converters.UserConverter;
import com.ricardo.pmapp.api.models.dtos.UserDto;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.services.UserServiceI;
import com.ricardo.pmapp.configurations.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestSecurityConfig.class
)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserConverter userConverter;

    @MockBean
    private UserServiceI userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenNotAuthUser_whenCreateUser_thenUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.ALL)
                .content(objectMapper.writeValueAsBytes(new UserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("developer")
    public void givenDeveloper_whenCreateUser_thenForbidden() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.ALL)
                .content(objectMapper.writeValueAsBytes(new UserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("developer")
    public void givenProjectManager_whenCreateUser_thenForbidden() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.ALL)
                .content(objectMapper.writeValueAsBytes(new UserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("administrator")
    public void givenAdmin_whenCreateUser_thenOk() throws Exception {
        UserDto userDto = new UserDto();
        User user = new User();
        when(userConverter.ToEntity(userDto)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(userConverter.ToDto(user)).thenReturn(userDto);

        mvc.perform(MockMvcRequestBuilders.post("/user")
                .accept(MediaType.ALL)
                .content(objectMapper.writeValueAsBytes(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("developer")
    public void givenDeveloper_whenGetUserByUsername_thenForbidden() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/test_user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("administrator")
    public void givenAdmin_whenGetUserByUsername_thenOk() throws Exception {
        UserDto retrievedUserDto = new UserDto();
        User retrievedUser = new User();
        when(userService.getByUsername("test_user")).thenReturn(retrievedUser);
        when(userConverter.ToDto(retrievedUser)).thenReturn(retrievedUserDto);
        mvc.perform(MockMvcRequestBuilders.get("/user/test_user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("developer")
    public void givenDeveloper_whenGetCurrentUser_thenOk() throws Exception {
        UserDto retrievedUserDto = new UserDto();
        User retrievedUser = new User();
        when(userService.getByUsername("developer")).thenReturn(retrievedUser);
        when(userConverter.ToDto(retrievedUser)).thenReturn(retrievedUserDto);
        mvc.perform(MockMvcRequestBuilders.get("/user/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
