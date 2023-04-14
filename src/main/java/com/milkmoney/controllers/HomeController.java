package com.milkmoney.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    @GetMapping("/")
    public String returnHome() {
        return "index-visitor";
    }


    @GetMapping("/index-visitor")
    public String returnVisitor() {
        return "index-visitor";
    }

    @GetMapping("/index-user")
    public String returnUser() throws JsonProcessingException {
        System.out.println("your inside the index user");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(user));
        return "index-user";

    }
}



