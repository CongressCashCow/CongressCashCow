package com.milkmoney.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public HomeController(PoliticianRepository politicianDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.api = api;
    }
    @GetMapping("/")
    public String returnHome() {
        return "index-visitor";
    }


    @GetMapping("/index-visitor")
    public String returnVisitor() {
        return "index-visitor";
    }

    @GetMapping("/index-user")
    public String returnUser(Model model) throws JsonProcessingException {
        System.out.println("your inside the index user");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(user));
        List<Trade> trades = api.getTrades();
        List<Trade> polTrades = new ArrayList<>();

        Politician p = politicianDAO.findByName("Virginia Foxx");
//        for (Trade t : p.getTrades()) {
//            System.out.println(t.getTicker());
//        }
        for (Trade t : trades) {
            if (t.getPolitician().getName().equals(p.getName())){
                polTrades.add(t);
            }
        }
        p.setTrades(polTrades);
        model.addAttribute("pol", p);
        return "index-user";

    }
}



