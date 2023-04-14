package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PoliticianController {
    private final PoliticianRepository politicianDAO;


    private APIService api;

//    static APIConnector api = new APIConnector();
    public PoliticianController(PoliticianRepository politicianDAO, APIService api) {

        this.politicianDAO = politicianDAO;
        this.api = api;

    }

    @GetMapping("/update")
    public String update(){

        api.update();
        api.updateImageURLs();
        savePols();
        return "redirect:/api";
    }
    @GetMapping("/api")
    public String apiView(Model model) {
            Set<Politician> pols = api.getPoliticians();

            model.addAttribute("pols",pols);


        return "api";
    }
    @GetMapping("/api/recent")
    public String apiRecent(Model model) {
//        Set<Politician> pols = api.getPoliticians();
        List<Trade> trades = api.getTrades();
        Set<Trade> recentTrades = new HashSet<>();
        int f = 0;
        for(Trade t : trades){
            if(f < 20){
                recentTrades.add(t);
                f++;
            }else {
                break;
            }
            System.out.println(t.getDate());
        }
//        System.out.println();
        model.addAttribute("trades",recentTrades);


        return "apirecent";
    }
    public void savePols(){
        politicianDAO.saveAll(api.getPoliticians());
    }
    private void getRecent(){
        getRecent(20);
    }
    private void getRecent(int count){
        List<Trade> trades = api.getTrades();
//        int position = 0;
//        while (position < count){
//
//
//            position++;
//        }
        for(Trade trade : trades){
            trade.getDate();
        }
    }

public static void main(String[] args) {

}

}
