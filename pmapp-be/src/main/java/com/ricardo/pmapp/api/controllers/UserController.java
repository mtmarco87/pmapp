package com.ricardo.pmapp.api.controllers;

import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserDeletionException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.api.converters.UserConverter;
import com.ricardo.pmapp.api.models.dtos.UserDto;
import com.ricardo.pmapp.exceptions.UserUpdateException;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.auth.CurrentUser;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.UserServiceI;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceI userService;

    private final UserConverter userConverter;

    public UserController(UserServiceI userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @RolesAllowed("Administrator")
    @PostMapping()
    public UserDto createUser(@RequestBody UserDto userDto) throws UserCreationException {
        return userConverter.ToDto(userService.create(userConverter.ToEntity(userDto)));
    }

    @GetMapping("/me")
    public UserDto getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws UserNotFoundException {
        return userConverter.ToDto(userService.getByUsername(userPrincipal.getUsername()));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return userConverter.ToDto(userService.getByUsername(username));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/getByEmail/{email}")
    public UserDto getUserByEmail(@PathVariable String email) throws UserNotFoundException {
        return userConverter.ToDto(userService.getByEmail(email));
    }

    @RolesAllowed("Administrator")
    @GetMapping("/all")
    public List<UserDto> findAllUsers() {
        return userService.findAll().stream().map(userConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed({"Administrator", "ProjectManager"})
    @GetMapping("/findByRole/{role}")
    public List<UserDto> findUsersByRole(@PathVariable Role role) {
        return userService.findByRole(role).stream().map(userConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed("Administrator")
    @GetMapping("/findByEmail/{email}")
    public List<UserDto> findUsersByEmail(@PathVariable String email) {
        return userService.findByEmail(email).stream().map(userConverter::ToDto).collect(Collectors.toList());
    }

    @RolesAllowed("Administrator")
    @PutMapping("/{username}")
    public UserDto updateUserByUsername(@RequestBody UserDto userDto, @PathVariable String username)
            throws UserNotFoundException, UserUpdateException {
        userDto.setUsername(username);
        return userConverter.ToDto(userService.update(userConverter.ToEntity(userDto)));
    }

    @RolesAllowed("Administrator")
    @DeleteMapping("/{username}")
    public void deleteUserByUsername(@PathVariable String username,
                                     @CurrentUser UserPrincipal userPrincipal)
            throws UserNotFoundException, UserDeletionException {
        userService.deleteByUsername(username, userPrincipal);
    }
}
