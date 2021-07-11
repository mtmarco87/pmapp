package com.ricardo.pmapp.service;

import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserDeletionException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.exceptions.UserUpdateException;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import com.ricardo.pmapp.persistence.repositories.UserRepository;
import com.ricardo.pmapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private User userMock;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldCreateAValidUser() throws UserCreationException {
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        userService.create(user);
        verify(userRepository, times(1)).findById(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldFailCreateUserWithoutMandatoryFields() {
        User user = new User(null, "", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        assertThrows(UserCreationException.class, () -> {
            userService.create(user);
        });
        verify(userRepository, never()).findById(any(String.class));
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldFailCreateUserWithoutStrongPassword() {
        User user = new User("dev", "dev@ricardo.ch", "test",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        assertThrows(UserCreationException.class, () -> userService.create(user));
        verify(userRepository, never()).findById(any(String.class));
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldFailCreateUserBecauseAlreadyExisting() {
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        assertThrows(UserCreationException.class, () -> userService.create(user));
        verify(userRepository, times(1)).findById(user.getUsername());
        verify(userRepository, never()).save(user);
    }


    @Test
    public void shouldRetrieveAUserByUsername() throws UserNotFoundException {
        when(userRepository.findById(userMock.getUsername())).thenReturn(Optional.of(userMock));
        userService.getByUsername(userMock.getUsername());
        verify(userRepository, times(1)).findById(userMock.getUsername());
    }


    @Test
    public void shouldRetrieveUsersByEmail() throws UserNotFoundException {
        when(userRepository.getByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
        userService.getByEmail(userMock.getEmail());
        verify(userRepository, times(1)).getByEmail(userMock.getEmail());
    }

    @Test
    public void shouldRetrieveAllUsers() {
        when(userRepository.findAll())
                .thenReturn(Collections.singletonList(userMock));
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldRetrieveUsersByRole() {
        Role testRole = Role.Administrator;
        when(userRepository.findByRole(testRole))
                .thenReturn(Collections.singletonList(userMock));
        userService.findByRole(testRole);
        verify(userRepository, times(1)).findByRole(testRole);
    }

    @Test
    public void shouldRetrieveUsersByEmailContaining() {
        String testEmail = "test@mail.com";
        when(userRepository.findByEmailContainsIgnoreCase(testEmail))
                .thenReturn(Collections.singletonList(userMock));
        userService.findByEmail(testEmail);
        verify(userRepository, times(1)).findByEmailContainsIgnoreCase(testEmail);
    }


    @Test
    public void shouldUpdateAValidUser() throws UserNotFoundException, UserUpdateException {
        User userToUpdate = new User("newDev", "the@company.com", "Test123",
                "John", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());
        User existingUser = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userToUpdate.getUsername())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        userService.update(userToUpdate);
        verify(userRepository, times(1)).findById(userToUpdate.getUsername());
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    public void shouldUpdateUserWithoutClearingMandatoryFields() throws UserNotFoundException, UserUpdateException {
        User userToUpdate = new User("dev", null, null,
                "Jack", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());
        User existingUser = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userToUpdate.getUsername())).thenReturn(Optional.of(existingUser));
        // Let's give back exactly the same input object
        when(userRepository.save(any(User.class)))
                .thenAnswer((Answer<User>) invocation -> (User) invocation.getArguments()[0]);

        User updatedUser = userService.update(userToUpdate);

        verify(userRepository, times(1)).findById(userToUpdate.getUsername());
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(updatedUser.getUsername(), existingUser.getUsername());
        assertEquals(updatedUser.getEmail(), existingUser.getEmail());
        assertEquals(updatedUser.getPassword(), existingUser.getPassword());
        assertEquals(updatedUser.getName(), userToUpdate.getName());
        assertEquals(updatedUser.getRole(), userToUpdate.getRole());
    }

    @Test
    public void shouldFailUpdateUserWithoutStrongPassword() {
        User userToUpdate = new User("dev", "dev@ricardo.ch", "abc",
                "Jack", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());
        User existingUser = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userToUpdate.getUsername())).thenReturn(Optional.of(existingUser));

        assertThrows(UserUpdateException.class, () -> userService.update(userToUpdate));

        verify(userRepository, times(1)).findById(userToUpdate.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldFailUpdateUserIfNotExisting() {
        User userToUpdate = new User("dev", "dev@ricardo.ch", "abc",
                "Jack", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userToUpdate.getUsername())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.update(userToUpdate));
        verify(userRepository, times(1)).findById(userToUpdate.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldDeleteAUser() throws UserNotFoundException, UserDeletionException {
        User userToDelete = new User("dev", "dev@ricardo.ch", "Password123",
                "Jack", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        Task aTask = new Task(1L, userToDelete, null, "description", 99,
                TaskStatus.InProgress, new Date());
        userToDelete.getTasks().add(aTask);


        when(userRepository.findById(userToDelete.getUsername())).thenReturn(Optional.of(userToDelete));
        doNothing().when(userRepository).deleteById(userToDelete.getUsername());
        userService.deleteByUsername(userToDelete.getUsername());
        verify(userRepository, times(1)).findById(userToDelete.getUsername());
        verify(userRepository, times(1)).deleteById(userToDelete.getUsername());

        assertNull(aTask.getAssignee());
    }


    @Test
    public void shouldFailDeleteAUserIfNotExisting() throws UserNotFoundException, UserDeletionException {
        User userToDelete = new User("dev", "dev@ricardo.ch", "Password123",
                "Jack", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userToDelete.getUsername())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteByUsername(userToDelete.getUsername()));
        verify(userRepository, times(1)).findById(userToDelete.getUsername());
        verify(userRepository, never()).deleteById(any(String.class));
    }
}
