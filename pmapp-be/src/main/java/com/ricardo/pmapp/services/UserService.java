package com.ricardo.pmapp.services;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ricardo.pmapp.exceptions.ExceptionMessages;
import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.exceptions.UserUpdateException;
import com.ricardo.pmapp.persistence.repositories.UserRepository;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceI {

    private final UserRepository userRepository;

    private final TaskServiceI taskService;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TaskServiceI taskService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user) throws UserCreationException {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new UserCreationException(ExceptionMessages.MISSING_MANDATORY_FIELDS_USER_CREATION);
        }

        if(!isPasswordValid(user.getPassword())){
            throw new UserCreationException(ExceptionMessages.INVALID_PASSWORD);
        }

        Optional<User> existingUser = userRepository.findById(user.getEmail());
        if (!existingUser.isPresent()) {
            // Encode password first
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } else {
            throw new UserCreationException(String.format(ExceptionMessages.USER_ALREADY_EXISTS, user.getEmail()));
        }
    }

    @Override
    public User getByUsername(String username) throws UserNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessages.USER_NOT_EXISTING,
                        username)));
    }

    @Override
    public User getByEmail(String email) throws UserNotFoundException {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionMessages.USER_NOT_EXISTING,
                        email)));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional
    public User update(User user) throws UserNotFoundException, UserUpdateException {
        // Check that provided User exists first
        User existingUser = getByUsername(user.getUsername());

        if (user.getPassword() != null && !StringUtils.isBlank(user.getPassword())) {

            // Set new password
            if(!isPasswordValid(user.getPassword())){
                throw new UserUpdateException(ExceptionMessages.INVALID_PASSWORD);
            }

            // Encode password first
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        } else {
            // Keep existing password
            user.setPassword(existingUser.getPassword());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username, UserPrincipal userPrincipal) throws UserNotFoundException {
        // Check that provided User exists first
        getByUsername(username);

        taskService.unassignByAssignee(username, userPrincipal);

        try {
            userRepository.deleteById(username);
        } catch (Exception ex) {
            throw new UserNotFoundException(String.format(ExceptionMessages.USER_NOT_EXISTING, username));
        }
    }

    private boolean isPasswordValid(String password){
        // Tests whether the passed password is valid:
        // min-length: 6 characters with at least 1 uppercase letter and 1 number
        return password.matches("^(?=.*\\d)(?=.*[A-Z])(.{6,})$");
    }
}
