package com.ricardo.pmapp.security.auth;

import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.UserServiceI;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ricardo.pmapp.exceptions.ExceptionMessages.USER_NOT_EXISTING;

/**
 * Used by spring during the login process, to check the password and if the user is enabled, locked, etc.
 * and to return the logged UserDetails
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceI userService;

    public CustomUserDetailsService(UserServiceI userService) {
        this.userService = userService;
    }

    /*
     * LoadUserByUsername will be called by AuthenticationManager.authenticate
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;
        try {
            user = userService.getByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(String.format(USER_NOT_EXISTING, username));
        }

        return UserPrincipal.create(user);
    }
}
