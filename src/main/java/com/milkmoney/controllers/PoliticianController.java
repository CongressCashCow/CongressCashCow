package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PoliticianController {
    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private APIService api;
    public PoliticianController(PoliticianRepository politicianDAO, UserRepository userDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.userDAO = userDAO;
        this.api = api;
    }

    @GetMapping("/update")
    public String update(){
        savePols();
        return "redirect:/api";
    }
    @GetMapping("/api")
    public String apiView(Model model) {
        Set<Politician> pols = api.getPoliticians();
        model.addAttribute("pols",pols);
        return "api";
    }
//    public String showProfilePage(Authentication authentication, Model model) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            // User is authenticated, add user details to the model and return the name of the profile view
//            User user = (User) authentication.getPrincipal();
//            model.addAttribute("username", user.getUsername());
//            model.addAttribute("email", user.getEmail());
//            return "profile";
//        } else {
//            // User is not authenticated, redirect to the login page
//            return "redirect:/login";
//        }
//    }
    @GetMapping("/api/recent")
    public String apiRecent(Authentication authentication, Model model) {
        List<Trade> recentTrades = new ArrayList<>();
        List<Trade> trades = api.getTrades();
        List<String> tradeNames = new ArrayList<>();
        int f = 0;
//        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());
        if (authentication != null && authentication.isAuthenticated()) {
            // User is authenticated, add user details to the model and return the name of the profile view
//            User user = (User) authentication.getPrincipal();
//            System.out.println("logged in");

            User currentUser = (User) authentication.getPrincipal();
            User fixedUser = userDAO.findByUsername(currentUser.getUsername());

            List<String> names = new ArrayList<>();

            for (Politician p : fixedUser.getPoliticians()) {
                names.add(p.getName());
            }
            model.addAttribute("names",names);
        } else {
            System.out.println("logged out");
        }

        for (Trade t : trades) {
            if (f < 20) {
                if (!tradeNames.contains(t.getPolitician().getName())) {
                    recentTrades.add(t);
                    tradeNames.add(t.getPolitician().getName());
                    f++;
                }
            } else {
                break;
            }
        }
        model.addAttribute("trades",recentTrades);

        return "apirecent";
    }
    @PostMapping("/api/recent")
    public String addFavorite(@RequestParam("pol_id") String name,@RequestParam("follow-btn") boolean follow) {

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

        return "redirect:/api/recent";
    }
    @GetMapping("/api/recent/censored")
    public String apiRecentLimited(Model model) {
        List<Trade> trades = api.getTrades();
        List<Trade> recentTrades = new ArrayList<>();
        int f = 0;
        for(Trade t : trades){
            if(f < 20){
                recentTrades.add(t);
                f++;
            }else {
                break;
            }
        }
        model.addAttribute("trades",recentTrades);
        return "apirecentcensored";
    }
    public void savePols(){
        politicianDAO.saveAll(api.getPoliticians());
    }


}
