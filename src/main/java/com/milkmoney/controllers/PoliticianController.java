package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PoliticianController {
    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private final APIService api;

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
    public String apiView(Authentication authentication, Model model, @RequestParam(required = false,value="searchbar") String searchQuery) {
        Set<Politician> pols = api.getPoliticians();
        Set<Politician> searchPols = new HashSet<>();
        int limit = 20;
        boolean isLimited = false;
        System.out.println(pols.size());

        if(searchQuery != null && searchQuery.trim().length() > 0){
            System.out.println(searchQuery);
            for(Politician p : politicianDAO.findByNameContaining(searchQuery)){
                for(Politician pol : pols){
                    if (pol.getName().equals(p.getName())){
                        searchPols.add(pol);
                    }
                }
            }
        }
        for(Politician p : searchPols){
            System.out.println(p.getName());
        }
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("logged in");

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
        Set<Politician> polsToShow = new HashSet<>();
        if(isLimited) {
            for (Politician p : pols) {
                if (polsToShow.size() < limit) {
                    polsToShow.add(p);
                } else {
                    break;
                }
            }
            System.out.println(polsToShow.size());
            model.addAttribute("pols",polsToShow);
        }else{
            if(searchPols.size() > 0){
                model.addAttribute("pols",searchPols);
            }else {
                model.addAttribute("pols", pols);
            }
        }

        return "api";
    }

    @GetMapping("/api/recent")
    public String apiRecent(Authentication authentication, Model model) {
//        String searchQuery = model.getAttribute("searchbar").toString();

        List<Trade> recentTrades = new ArrayList<>();
        List<Trade> trades = api.getTrades();
        List<String> tradeNames = new ArrayList<>();
        int f = 0;


        if (authentication != null && authentication.isAuthenticated()) {


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

        return "redirect:/api";
    }

    public void savePols(){
        politicianDAO.saveAll(api.getPoliticians());
    }

//    private List<Politician> searchPols(String query){
//        politicianDAO.findAll();
//    }




}
