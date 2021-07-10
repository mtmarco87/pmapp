package com.ricardo.pmapp.initscript;

import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.exceptions.TaskCreationException;
import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.persistence.models.entities.Task;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.persistence.models.enums.TaskStatus;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.TaskServiceI;
import com.ricardo.pmapp.services.UserServiceI;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class InitScript implements ApplicationListener<ContextRefreshedEvent> {

    private final UserServiceI userService;

    private final TaskServiceI taskService;

    private final AppConfig appConfig;

    public InitScript(UserServiceI userService, TaskServiceI taskService, AppConfig appConfig) {
        this.userService = userService;
        this.taskService = taskService;
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
        // Method used to init the DB


        // Sample users creation
        User usr1 = new User();
        usr1.setEmail("user1@ricardo.ch");
        usr1.setUsername("admin");
        usr1.setName("John");
        usr1.setSurname("Wick");
        usr1.setRole(Role.Administrator);
        usr1.setPassword("Test123");

        try {
            usr1 = userService.create(usr1);
        } catch (UserCreationException e) {
            e.printStackTrace();
        }

        User usr2 = new User();
        usr2.setEmail("user2@ricardo.ch");
        usr2.setUsername("projman");
        usr2.setName("Mick");
        usr2.setSurname("Smith");
        usr2.setRole(Role.ProjectManager);
        usr2.setPassword("Test123");

        try {
            usr2 = userService.create(usr2);
        } catch (UserCreationException e) {
            e.printStackTrace();
        }

        User usr3 = new User();
        usr3.setEmail("user3@ricardo.ch");
        usr3.setUsername("dev");
        usr3.setName("Nick");
        usr3.setSurname("Moore");
        usr3.setRole(Role.Developer);
        usr3.setPassword("Test123");

        try {
            usr3 = userService.create(usr3);
        } catch (UserCreationException e) {
            e.printStackTrace();
        }


        // Sample Tasks creation

        UserPrincipal admin = UserPrincipal.create(usr1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Task task1 = new Task();
        task1.setAssignee(usr3);
        task1.setDeadline(formatter.parse("2021-12-12"));
        task1.setDescription("Task1 test");
        task1.setProgress(10);
        task1.setStatus(TaskStatus.New);

        Task task2 = new Task();
        task2.setAssignee(usr2);
        task2.setDeadline(formatter.parse("2020-03-12"));
        task2.setDescription("Task2 test");
        task2.setProgress(66);
        task2.setStatus(TaskStatus.InProgress);

        Task task3 = new Task();
        task3.setAssignee(usr1);
        task3.setDeadline(formatter.parse("2020-03-12"));
        task3.setDescription("Task3 test");
        task3.setProgress(99);
        task3.setStatus(TaskStatus.Completed);

        Task task4 = new Task();
        task4.setDeadline(formatter.parse("2000-03-12"));
        task4.setDescription("Task4 test");
        task4.setProgress(0);
        task4.setStatus(TaskStatus.New);

        try {
            taskService.create(task1, admin);
            task1.setCode(null);
            taskService.create(task1, admin);
            task1.setCode(null);
            taskService.create(task1, admin);
            task1.setCode(null);
            taskService.create(task2, admin);
            task2.setCode(null);
            taskService.create(task2, admin);
            task2.setCode(null);
            taskService.create(task3, admin);
            task3.setCode(null);
            taskService.create(task4, admin);
            task4.setCode(null);
            taskService.create(task4, admin);
            task4.setCode(null);
        } catch (TaskCreationException e) {
            e.printStackTrace();
        }


        // Sample Projects creation

    }
}
