package com.ricardo.pmapp.persistence.models.entities;

import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * This entity represents a Task
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@ToString
@EqualsAndHashCode
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long code;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "assignee", referencedColumnName = "username")
    private User assignee;

    private String description;

    private int progress;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Date deadline;
}
