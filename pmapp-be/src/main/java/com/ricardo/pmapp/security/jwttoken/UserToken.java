package com.ricardo.pmapp.security.jwttoken;

import com.ricardo.pmapp.persistence.models.enums.Role;
import com.ricardo.pmapp.security.models.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserToken {

    private String username;

    private String email;

    private Role role;

    public UserToken(UserPrincipal userPrincipal) {
        this.username = userPrincipal.getUsername();
        this.email = userPrincipal.getEmail();
        this.role = userPrincipal.getRole();
    }
}
