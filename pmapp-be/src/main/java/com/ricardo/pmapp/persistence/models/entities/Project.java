package com.ricardo.pmapp.persistence.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity represents a Project
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
@ToString
@EqualsAndHashCode
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long code;

    private String name;

    @OneToMany(mappedBy="project")
    private List<Task> tasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="project_manager", nullable = false)
    private User projectManager;
}
