package com.milkmoney.controllers;

import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

//(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



}
