package com.ricardo.pmapp.services;

import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.exceptions.UserUpdateException;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;

import java.util.List;

/**
 * Interface providing basics CRUD operation on a {@link User}
 */

public interface UserServiceI {

    User create(User user) throws UserCreationException;

    User getByUsername(String username) throws UserNotFoundException;

    User getByEmail(String email) throws UserNotFoundException;

    List<User> findAll();

    List<User> findByRole(Role role);

    User update(User user) throws UserNotFoundException, UserUpdateException;

    void deleteByUsername(String username, UserPrincipal userPrincipal) throws UserNotFoundException;

}
