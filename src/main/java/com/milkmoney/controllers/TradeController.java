package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
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
    public String viewTrades(Model model, @RequestParam(required = false,value="searchbar") String searchQuery){
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

        return "trades";
    }
}
