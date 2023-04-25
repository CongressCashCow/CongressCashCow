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
    public String viewTrades(Model model, @RequestParam(required = false,value="searchbar") String searchQuery, @RequestParam(required = false,value="page",defaultValue = "1") String page,@RequestParam(required = false,value="arrow-btn") String pageDir){
        List<Trade> trades = api.getTrades();
        List<Trade> middleman = new ArrayList<>();
        String path = null;
        int limit= 201;
        if(page == null || page.equals("")){
            page = "1";
        }
        int pageInt = Integer.parseInt(page);

        if(pageDir != null){
            if(pageDir.equals("right")){
                pageInt++;
            }else {
                pageInt--;
            }

        }
        if(searchQuery != null && searchQuery.trim().length() > 0){
            path = String.format("?searchbar=%s&page=%s",searchQuery,page);
            for(Trade t : trades){
                if(t.getTicker().equalsIgnoreCase(searchQuery)){
                    middleman.add(t);
                }
            }

        }else {
            path = String.format("?page=%s",page);
            middleman = trades;
        }
        List<Trade> out = new ArrayList<>();

        if(middleman.size() > limit){
            int offset = (pageInt - 1) * limit;
            for(Trade t : middleman){
//                System.out.println(middleman.indexOf(t));
                if(middleman.indexOf(t) > offset && middleman.indexOf(t) < offset + limit ){
                    out.add(t);
                }
            }
        }else{
            for (Trade t : middleman){
                out.add(t);
            }
        }

        model.addAttribute("trades", out);

        model.addAttribute("path", path);
        model.addAttribute("tradeTotal", middleman.size());
        return "trades";
    }
}
