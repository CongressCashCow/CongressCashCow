package com.milkmoney.controllers;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthenticationController {
    @GetMapping("/login")
    public String LoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }



}

