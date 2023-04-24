package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.models.Politician;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserProfileController {

    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;

    public UserProfileController(UserRepository userDAO, PoliticianRepository politicianDAO) {

        this.userDAO = userDAO;
        this.politicianDAO = politicianDAO;
    }

    @GetMapping("/user-profile")
    public String userProfilePage(Model model) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();

        List<Politician> followedPoliticians = fixedUser.getPoliticians();
        model.addAttribute("followedPoliticians", followedPoliticians);

        return "user-profile";
    }
    @PostMapping("/user-profile")
    public String rmFavorite(@RequestParam("pol_id") String name, @RequestParam("follow-btn") boolean follow) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        boolean isPresent = false;
        if(follow){
            for(Politician p : fixedUser.getPoliticians()){
                if(p.getName().equals(name)){
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent){
                fixedUser.addPolitician(politicianDAO.findByName(name));
                userDAO.save(fixedUser);
            }
        }else{
            fixedUser.removePolitician(politicianDAO.findByName(name));
            userDAO.save(fixedUser);
        }

        return "redirect:/user-profile";
    }


}
