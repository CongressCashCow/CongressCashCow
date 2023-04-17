package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PoliticianProfileController {
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public PoliticianProfileController(PoliticianRepository politicianDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.api = api;
    }

    @GetMapping("/politician-profile")
    public String politicianProfilePage(Model model) {
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

        return "politician-profile";
    }
}
