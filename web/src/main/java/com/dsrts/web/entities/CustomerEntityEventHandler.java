package com.dsrts.web.entities;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@RepositoryEventHandler(CustomerEntity.class)
public class CustomerEntityEventHandler {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @HandleAfterCreate
    public void handleCustomerCreated(CustomerEntity customer) {

        String username = customer.getEmail();
        String password = passwordEncoder.encode("password");

        User userDetails = new User(username, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        userDetailsManager.createUser(userDetails);
    }
}
