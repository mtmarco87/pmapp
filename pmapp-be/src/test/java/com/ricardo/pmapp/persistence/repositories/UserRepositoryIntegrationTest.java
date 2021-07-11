package com.ricardo.pmapp.persistence.repositories;

import com.ricardo.pmapp.PmAppApplication;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PmAppApplication.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void cleanDB(){
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void shouldInsertAndRetrieveAUserByUsername(){
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        User insertedUser = userRepository.save(user);
        assertNotNull(insertedUser.getUsername());
        User retrievedUser = userRepository.getById(insertedUser.getUsername());
        assertEquals(insertedUser, retrievedUser);
    }

    @Test
    @Transactional
    public void shouldInsertAndRetrieveAUserByRole(){
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        userRepository.save(user);
        List<User> retrievedUsers = userRepository.findByRole(user.getRole());
        assertEquals(1, retrievedUsers.size());
        assertEquals(user, retrievedUsers.get(0));
    }

    @Test
    @Transactional
    public void shouldInsertAndRetrieveByEmailContaining(){
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        User user2 = new User("pm", "pm@ricardo.ch", "Password123",
                "Matt", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());
        userRepository.save(user);
        userRepository.save(user2);
        List<User> retrievedUsers = userRepository.findByEmailContainsIgnoreCase("DEV@");
        assertEquals(1, retrievedUsers.size());
        retrievedUsers = userRepository.findByEmailContainsIgnoreCase("ricardo");
        assertEquals(2, retrievedUsers.size());
    }

    @Test
    @Transactional
    public void shouldInsertAndRetrieveAllUsers(){
        User user = new User("dev", "dev@ricardo.ch", "Password123",
                "John", "Doe", Role.Developer, new ArrayList<>(), new ArrayList<>());
        User user2 = new User("pm", "pm@ricardo.ch", "Password123",
                "Matt", "Doe", Role.ProjectManager, new ArrayList<>(), new ArrayList<>());
        userRepository.save(user);
        userRepository.save(user2);
        List<User> retrievedUsers = userRepository.findAll();
        assertEquals(2, retrievedUsers.size());
    }
}
