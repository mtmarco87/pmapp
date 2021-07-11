package com.ricardo.pmapp.persistence.models.entities;

import com.ricardo.pmapp.persistence.models.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity represents an User
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @Column(nullable = false)
    private String username;

    @Email
    @Column(nullable = false)
    private String email;

    private String password;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy="assignee")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy="projectManager")
    private List<Project> projects = new ArrayList<>();
}
