package com.example.springsecurityintegrationtests;


import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

    @GetMapping("/public/hello")
    public List<String> publicHello() {
        return Arrays.asList("Hello", "World", "from", "Public");
    }

    @GetMapping("/private/hello")
    public List<String> privateHello() {
        return Arrays.asList("Hello", "World", "from", "Private");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private/role-admin")
    public String getUsernameAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }

}
