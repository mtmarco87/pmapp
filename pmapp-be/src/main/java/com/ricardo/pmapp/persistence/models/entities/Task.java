package com.ricardo.pmapp.persistence.models.entities;

import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @ManyToOne
    @JoinColumn(name="assignee")
    private User assignee;

    @ManyToOne
    @JoinColumn(name="project")
    private Project project;

    private String description;

    @Min(0)
    @Max(100)
    private int progress;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Date deadline;
}
