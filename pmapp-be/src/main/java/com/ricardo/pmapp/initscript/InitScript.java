package com.ricardo.pmapp.initscript;

import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitScript implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;

    private final AppConfig appConfig;

    public InitScript(UserService userService, AppConfig appConfig) {
        this.userService = userService;
        this.appConfig = appConfig;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (this.appConfig.getInitScript().isEnabled()) {
            InitDB();
        }
    }

    private void InitDB() {
        //method used to init to the db
        User usr1 = new User();
        usr1.setEmail("user1@ricardo.ch");
        usr1.setUsername("user1");
        usr1.setName("John");
        usr1.setSurname("Wick");
        usr1.setRole(Role.Administrator);
        usr1.setPassword("test123");

        try {
            userService.create(usr1);
        } catch (UserCreationException e) {
            e.printStackTrace();
        }
    }
}
