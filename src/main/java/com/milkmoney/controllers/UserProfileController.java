package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class UserProfileController {

    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private PasswordEncoder passwordEncoder;
    private final APIService api;

    public UserProfileController(UserRepository userDAO, PoliticianRepository politicianDAO, APIService api, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
        this.politicianDAO = politicianDAO;
        this.api = api;
    }

    @GetMapping("/user-profile")
    public String userProfilePage(Model model) {
        Set<Politician> apiPols = api.getPoliticians();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        List<Politician> followedPoliticians = new ArrayList<>();
        for (Politician pol : apiPols) {
            for (Politician myPol : fixedUser.getPoliticians()) {
                if (pol.getName().equals(myPol.getName())) {
                    followedPoliticians.add(pol);
                }
            }

        }
        model.addAttribute("followedPoliticians", followedPoliticians);
        model.addAttribute("user",fixedUser);
        return "user-profile";
    }
    @GetMapping("/edit")
    public String editUser(Model model) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();

        model.addAttribute("user",fixedUser);
        return "EditProfile";
    }
    @PostMapping("/edit")
    public String saveUser(Model model, @ModelAttribute User user) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        boolean hasChanged = false;
        if(!user.getUsername().equals(fixedUser.getUsername())){
            fixedUser.setUsername(user.getUsername());
            hasChanged = true;
        }
        if(!user.getEmail().equals(fixedUser.getEmail())){
            fixedUser.setEmail(user.getEmail());
            hasChanged = true;
        }
        String hashedPW = passwordEncoder.encode(user.getPassword());
        if(user.getPassword().length() > 0) {
            if (!hashedPW.equals(fixedUser.getPassword())) {
                fixedUser.setPassword(hashedPW);
                hasChanged = true;
            }
        }
        if(hasChanged){
            userDAO.save(fixedUser);
        }

        return "redirect:/user-profile";
    }
    @PostMapping("/delete")
    public String deleteAcc(HttpServletRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        userDAO.delete(fixedUser);
        HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();

        if(session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @PostMapping("/user-profile")
    public String rmFavorite(Model model, @RequestParam("pol_id") String name, @RequestParam("follow-btn") boolean follow) {

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
