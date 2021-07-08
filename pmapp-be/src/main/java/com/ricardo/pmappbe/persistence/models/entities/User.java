package com.ricardo.pmappbe.persistence.models.entities;

import com.ricardo.pmappbe.persistence.models.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * This entity represents an User
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
@ToString
public class User {

    @Column(nullable = false)
    private String username;

    @Id
    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private String surname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
