package com.ricardo.pmapp.persistence.repositories;

import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> getByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByEmailContainsIgnoreCase(String email);
}
