package com.ricardo.pmapp.security.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * CurrentUser is an interface that override @AuthenticationPrincipal and return the currentLoggedUser from
 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 **/
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
