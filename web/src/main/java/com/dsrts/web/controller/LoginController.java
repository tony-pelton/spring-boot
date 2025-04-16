package com.dsrts.web.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    private final JdbcTemplate jdbcTemplate;

    public LoginController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public String login(Model model) {
        List<String> emails = jdbcTemplate.queryForList(
            "SELECT username FROM users ORDER BY username", String.class
        );
        model.addAttribute("emails", emails);
        return "login";
    }
}
