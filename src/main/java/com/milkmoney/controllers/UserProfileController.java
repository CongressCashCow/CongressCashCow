package com.milkmoney.controllers;

import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.models.Politician;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserProfileController {

    private final UserRepository userDAO;

    public UserProfileController(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/user-profile")
    public String userProfilePage(Model model) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();

        List<Politician> followedPoliticians = fixedUser.getPoliticians();
        model.addAttribute("followedPoliticians", followedPoliticians);

        return "user-profile";
    }

}
