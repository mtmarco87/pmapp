package com.ricardo.pmapp.services;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ricardo.pmapp.exceptions.*;
import com.ricardo.pmapp.persistence.repositories.UserRepository;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceI {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user) throws UserCreationException {
        if (user.getUsername() == null || StringUtils.isBlank(user.getUsername()) ||
                user.getEmail() == null || StringUtils.isBlank(user.getEmail()) ||
                user.getPassword() == null || StringUtils.isBlank(user.getPassword())) {
            // Check for mandatory required fields
            throw new UserCreationException(ExceptionMessages.MISSING_MANDATORY_FIELDS_USER_CREATION);
        } else if (!isPasswordValid(user.getPassword())) {
            // Check for strong valid password
            throw new UserCreationException(ExceptionMessages.INVALID_PASSWORD);
        }

        Optional<User> existingUser = userRepository.findById(user.getUsername());
        if (!existingUser.isPresent()) {
            // Encode password first and then store it
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
    public List<User> findByEmail(String email) {
        return userRepository.findByEmailContainsIgnoreCase(email);
    }


    @Override
    @Transactional
    public User update(User user) throws UserNotFoundException, UserUpdateException {
        // Check that provided User exists first
        User existingUser = getByUsername(user.getUsername());

        if (user.getEmail() == null || StringUtils.isBlank(user.getEmail())) {
            // If null keep existing email
            user.setEmail(existingUser.getEmail());
        }

        if (user.getPassword() != null && !StringUtils.isBlank(user.getPassword())) {
            // Set new password if valid
            if (!isPasswordValid(user.getPassword())) {
                throw new UserUpdateException(ExceptionMessages.INVALID_PASSWORD);
            }

            // Encode password first and then store it
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        } else {
            // Keep existing password
            user.setPassword(existingUser.getPassword());
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteByUsername(String username)
            throws UserNotFoundException, UserDeletionException {
        // Check that provided User exists first
        User existingUser = getByUsername(username);

        // Clear relationships
        existingUser.getTasks().forEach(task -> task.setAssignee(null));

        try {
            userRepository.deleteById(username);
        } catch (Exception ex) {
            throw new UserDeletionException(ExceptionMessages.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isPasswordValid(String password) {
        // Tests whether the passed password is valid:
        // min-length: 6 characters with at least 1 uppercase letter and 1 number
        return password.matches("^(?=.*\\d)(?=.*[A-Z])(.{6,})$");
    }
}
