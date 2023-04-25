package com.milkmoney.controllers;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TradeController {

    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private final APIService api;

    public TradeController(PoliticianRepository politicianDAO, UserRepository userDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.userDAO = userDAO;
        this.api = api;
    }
    @GetMapping("/trades")
    public String viewTrades(Authentication authentication, Model model, @RequestParam(required = false,value="searchbar") String searchQuery){
        List<Trade> trades = api.getTrades();
        List<Trade> searchResults = new ArrayList<>();
        System.out.println(searchQuery);
        if(searchQuery != null && searchQuery.trim().length() > 0){
            for(Trade t : trades){
                if(t.getTicker().equalsIgnoreCase(searchQuery)){
                    searchResults.add(t);
                }
            }
            model.addAttribute("trades", searchResults);
        }else{
            model.addAttribute("trades", trades);
        }

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("logged in");

            User currentUser = (User) authentication.getPrincipal();
            User fixedUser = userDAO.findByUsername(currentUser.getUsername());

            List<String> names = new ArrayList<>();

            for (Politician p : fixedUser.getPoliticians()) {
                names.add(p.getName());
            }
            model.addAttribute("names", names);
        }
            return "trades";

    }

    @PostMapping("/trades")
    public String addFavorite(@RequestParam("ticker") String ticker, @RequestParam("pol_id") String name,@RequestParam("follow-btn") boolean follow) {

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

        String out=String.format("redirect:/trades?searchbar=%s", ticker);

        return out;

    }

}
