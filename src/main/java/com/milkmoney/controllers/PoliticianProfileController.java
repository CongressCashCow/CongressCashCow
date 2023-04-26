package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PoliticianProfileController {

    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public PoliticianProfileController(PoliticianRepository politicianDAO, UserRepository userDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.userDAO = userDAO;
        this.api = api;
    }

    @GetMapping("/politician-profile")
    public String politicianProfilePage(Model model, @RequestParam(required = false,value="pol") String searchQuery) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();

        List<Trade> polTrades;
        Politician p;
        if(searchQuery != null && searchQuery.trim().length() > 0){
            System.out.println(searchQuery);
            p = politicianDAO.findByName(searchQuery);
        }else{
            p = politicianDAO.findByName("Virginia Foxx");
        }


        polTrades = api.getPoliticianTrades(p.getName());
        if(fixedUser.getPoliticians().contains(p)) {
            model.addAttribute("follow", true);
        } else {
            model.addAttribute("follow", false);
        }
        p.setTrades(polTrades);
        model.addAttribute("pol", p);

        return "politician-profile";
    }




    @PostMapping("/politician-profile")
    public String addFavorite(@RequestParam("pol_id") String name, @RequestParam("follow-btn") boolean follow) {

        System.out.println(name);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        boolean isPresent = false;
        System.out.println(fixedUser.getUsername());
        System.out.println("follow: " + follow);
        if(follow){
            for(Politician p : fixedUser.getPoliticians()){
                if(p.getName().equals(name)){
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent){
                System.out.println(fixedUser.getPoliticians().size());
                fixedUser.addPolitician(politicianDAO.findByName(name));
                System.out.println(fixedUser.getPoliticians().size());
                userDAO.save(fixedUser);
                System.out.println("added");
            }
        }else{

            fixedUser.removePolitician(politicianDAO.findByName(name));
            userDAO.save(fixedUser);
            System.out.println("removed");
        }
        String output = String.format("redirect:/politician-profile?pol=%s",name);
        return output;
    }


}
