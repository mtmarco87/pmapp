package com.ricardo.pmapp.utils;

import com.ricardo.pmapp.api.converters.TaskConverter;
import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.exceptions.ProjectCreationException;
import com.ricardo.pmapp.exceptions.TaskCreationException;
import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.persistence.models.entities.Project;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.ProjectServiceI;
import com.ricardo.pmapp.services.TaskServiceI;
import com.ricardo.pmapp.services.UserServiceI;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Script for DB pre-population. This Component hooks on application startup.
 */

@Component
public class InitScript implements ApplicationListener<ContextRefreshedEvent> {

    private final UserServiceI userService;

    private final TaskServiceI taskService;

    private final ProjectServiceI projectService;

    private final TaskConverter taskConverter;

    private final AppConfig appConfig;

    public InitScript(UserServiceI userService, TaskServiceI taskService, ProjectServiceI projectService,
                      TaskConverter taskConverter, AppConfig appConfig) {
        this.userService = userService;
        this.taskService = taskService;
        this.projectService = projectService;
        this.taskConverter = taskConverter;
        this.appConfig = appConfig;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (this.appConfig.getInitScript().isEnabled()) {
            try {
                InitDB();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void InitDB() throws ParseException {

        // Sample users creation
        User usr1 = new User();
        usr1.setEmail("user1@ricardo.ch");
        usr1.setUsername("admin");
        usr1.setName("John");
        usr1.setSurname("Wick");
        usr1.setRole(Role.Administrator);
        usr1.setPassword("Test123");

        User usr2 = new User();
        usr2.setEmail("user2@ricardo.ch");
        usr2.setUsername("projman");
        usr2.setName("Michael");
        usr2.setSurname("Smith");
        usr2.setRole(Role.ProjectManager);
        usr2.setPassword("Test123");

        User usr3 = new User();
        usr3.setEmail("user3@ricardo.ch");
        usr3.setUsername("projman2");
        usr3.setName("Simon");
        usr3.setSurname("Fisher");
        usr3.setRole(Role.ProjectManager);
        usr3.setPassword("Test123");

        User usr4 = new User();
        usr4.setEmail("user4@ricardo.ch");
        usr4.setUsername("dev");
        usr4.setName("Nick");
        usr4.setSurname("Moore");
        usr4.setRole(Role.Developer);
        usr4.setPassword("Test123");

        User usr5 = new User();
        usr5.setEmail("user5@ricardo.ch");
        usr5.setUsername("dev2");
        usr5.setName("Matt");
        usr5.setSurname("Jackson");
        usr5.setRole(Role.Developer);
        usr5.setPassword("Test123");

        try {
            usr1 = userService.create(usr1);
            usr2 = userService.create(usr2);
            usr3 = userService.create(usr3);
            usr4 = userService.create(usr4);
            usr5 = userService.create(usr5);
        } catch (UserCreationException e) {
            e.printStackTrace();
        }


        // Sample Projects creation

        Project proj1 = new Project();
        proj1.setName("Project1 test");
        proj1.setProjectManager(usr2);

        Project proj2 = new Project();
        proj2.setName("Project2 test");
        proj2.setProjectManager(usr1);

        try {
            proj1 = projectService.create(proj1);
            proj2 = projectService.create(proj2);
        } catch (ProjectCreationException e) {
            e.printStackTrace();
        }

        // Sample Tasks creation

        UserPrincipal admin = UserPrincipal.create(usr1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Task task1 = new Task();
        task1.setAssignee(usr4);
        task1.setDeadline(formatter.parse("2021-12-12"));
        task1.setDescription("Task1 test");
        task1.setProgress(10);
        task1.setStatus(TaskStatus.New);
        task1.setProject(proj1);

        Task task2 = new Task();
        task2.setAssignee(usr5);
        task2.setDeadline(formatter.parse("2022-04-01"));
        task2.setDescription("Task2 test");
        task2.setProgress(0);
        task2.setStatus(TaskStatus.New);
        task2.setProject(proj1);

        Task task3 = new Task();
        task3.setAssignee(usr2);
        task3.setDeadline(formatter.parse("2022-03-12"));
        task3.setDescription("Task3 test");
        task3.setProgress(66);
        task3.setStatus(TaskStatus.InProgress);
        task3.setProject(proj2);

        Task task4 = new Task();
        task4.setAssignee(usr1);
        task4.setDeadline(formatter.parse("2022-08-10"));
        task4.setDescription("Task4 test");
        task4.setProgress(99);
        task4.setStatus(TaskStatus.Completed);
        task4.setProject(proj2);

        Task task5 = new Task();
        task5.setDeadline(formatter.parse("2023-01-01"));
        task5.setDescription("Task5 test");
        task5.setProgress(0);
        task5.setStatus(TaskStatus.New);
        task5.setProject(proj1);

        Task task6 = CloneTask(task2);
        task6.setDescription("Task6 test");

        Task task7 = CloneTask(task3);
        task7.setDescription("Task7 test");

        Task task8 = CloneTask(task5);
        task8.setDescription("Task8 test");

        try {
            task1 = taskService.create(task1, admin);
            task2 = taskService.create(task2, admin);
            task3 = taskService.create(task3, admin);
            task4 = taskService.create(task4, admin);
            task5 = taskService.create(task5, admin);
            task6 = taskService.create(task6, admin);
            task7 = taskService.create(task7, admin);
            task8 = taskService.create(task8, admin);
        } catch (TaskCreationException | AccessDeniedException e) {
            e.printStackTrace();
        }
    }

    private Task CloneTask(Task task) {
        Task newTask = taskConverter.ToEntity(taskConverter.ToDto(task));
        newTask.setCode(null);
        return newTask;
    }
}
