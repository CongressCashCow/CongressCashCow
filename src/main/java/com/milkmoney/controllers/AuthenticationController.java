package com.milkmoney.controllers;
import com.milkmoney.Repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthenticationController {

    private UserRepository userDao;

    public AuthenticationController(UserRepository userDao) {
        this.userDao = userDao;
    }
    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }


}

