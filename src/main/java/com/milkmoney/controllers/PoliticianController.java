package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PoliticianController {
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public PoliticianController(PoliticianRepository politicianDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.api = api;
    }

    @GetMapping("/update")
    public String update() {
        api.update();
        api.updateImageURLs();
        savePols();
        return "redirect:/api";
    }

    @GetMapping("/api")
    public String apiView(Model model) {
        Set<Politician> pols = api.getPoliticians();
        model.addAttribute("pols", pols);
        return "api";
    }

    @GetMapping("/api/recent")
    public String apiRecent(Model model) {
        List<Trade> trades = api.getTrades();
        List<Trade> recentTrades = new ArrayList<>();
        int f = 0;
        for (Trade t : trades) {
            if (f < 20) {
                recentTrades.add(t);
                f++;
            } else {
                break;
            }
        }
        model.addAttribute("trades", recentTrades);
        return "apirecent";
    }

    @GetMapping("/api/recent/censored")
    public String apiRecentLimited(Model model) {
        List<Trade> trades = api.getTrades();
        List<Trade> recentTrades = new ArrayList<>();
        int f = 0;
        for (Trade t : trades) {
            if (f < 20) {
                recentTrades.add(t);
                f++;
            } else {
                break;
            }
        }
        model.addAttribute("trades", recentTrades);
        return "apirecentcensored";
    }

    public void savePols() {
        politicianDAO.saveAll(api.getPoliticians());
    }

}
