package com.ricardo.pmapp.configurations;

import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;


@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        // Mocked UserPrincipals preparation

        User devUser = new User();
        devUser.setUsername("developer");
        devUser.setEmail("dev@company.com");
        devUser.setPassword("password");
        devUser.setRole(Role.Developer);

        UserPrincipal devUserPrincipal = UserPrincipal.create(devUser);

        User pmUser = new User();
        pmUser.setUsername("project_manager");
        pmUser.setEmail("pm@company.com");
        pmUser.setPassword("password");
        pmUser.setRole(Role.ProjectManager);

        UserPrincipal pmUserPrincipal = UserPrincipal.create(pmUser);

        User adminUser = new User();
        adminUser.setUsername("administrator");
        adminUser.setEmail("admin@company.com");
        adminUser.setPassword("password");
        adminUser.setRole(Role.Administrator);

        UserPrincipal adminUserPrincipal = UserPrincipal.create(adminUser);

        return new TestUserDetailsService(
                Arrays.asList(devUserPrincipal, pmUserPrincipal, adminUserPrincipal),
                devUserPrincipal
        );
    }

    // Custom InMemory UserDetailsService
    private static class TestUserDetailsService implements UserDetailsService {
        private final List<UserPrincipal> userPrincipalList;

        private final UserPrincipal fallbackUser;

        public TestUserDetailsService(List<UserPrincipal> userPrincipalList, UserPrincipal fallbackUser) {
            this.userPrincipalList = userPrincipalList;
            this.fallbackUser = fallbackUser;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userPrincipalList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst().orElse(fallbackUser);
        }
    }
}
