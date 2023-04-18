package com.milkmoney.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public HomeController(PoliticianRepository politicianDAO, UserRepository userDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.userDAO = userDAO;
        this.api = api;
    }
    @GetMapping("/")
    public String returnHome() {

        return "redirect:/index";
    }


//    @GetMapping("/index-visitor")
//    public String returnVisitor(Authentication authentication, Model model) {
//        List<Trade> recentTrades = new ArrayList<>();
//        List<Trade> trades = api.getTrades();
//        List<String> tradeNames = new ArrayList<>();
//        int f = 0;
//
////        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
////            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());
//        if (authentication != null && authentication.isAuthenticated()) {
//            // User is authenticated, add user details to the model and return the name of the profile view
////            User user = (User) authentication.getPrincipal();
////            System.out.println("logged in");
//
//            User currentUser = (User) authentication.getPrincipal();
//            User fixedUser = userDAO.findByUsername(currentUser.getUsername());
//
//            List<String> names = new ArrayList<>();
//
//            for (Politician p : fixedUser.getPoliticians()) {
//                names.add(p.getName());
//            }
//            model.addAttribute("names",names);
//        } else {
//            System.out.println("logged out");
//        }
//
//        for (Trade t : trades) {
//            if (f < 20) {
//                if (!tradeNames.contains(t.getPolitician().getName())) {
//                    recentTrades.add(t);
//                    tradeNames.add(t.getPolitician().getName());
//                    f++;
//                }
//            } else {
//                break;
//            }
//        }
//
//        model.addAttribute("trades",recentTrades);
//        return "index-visitor";
//    }

    @GetMapping("/index")
    public String returnUser(Authentication authentication, Model model) throws JsonProcessingException {
        System.out.println("your inside the index user");
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(mapper.writeValueAsString(user));

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

        return "index-user";

    }
}



