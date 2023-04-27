package com.milkmoney.controllers;
import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.data.domain.*;
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

    Page<Trade> toPage(List<Trade> list, int pagesize, int pageNo) {
        int totalpages = list.size() / pagesize;
        Pageable pageable = PageRequest.of(pageNo, pagesize, Sort.by("ticker"));
        int max = pageNo >= totalpages? list.size():pagesize*(pageNo+1);
        int min = pageNo > totalpages? max:pagesize*pageNo;
        return new PageImpl<Trade>(list.subList(min, max), pageable, list.size());
    }

    @GetMapping("/trades")
    public String viewTrades(Authentication authentication, Model model, @RequestParam(required = false,value="searchbar") String searchQuery, @RequestParam(required = false,value="page",defaultValue = "0") int pageNo){
        List<Trade> trades = api.getTrades();
        List<Trade> searchResults = new ArrayList<>();

        int pageLimit = 200;
        if(pageNo < 0){
            pageNo = 0;
        }else if(pageNo > trades.size()/pageLimit){
            pageNo = (int) Math.floor(trades.size()/pageLimit);
        }
        if(searchQuery != null && searchQuery.trim().length() > 0){
            for(Trade t : trades){
                if(t.getTicker().equalsIgnoreCase(searchQuery)){
                    searchResults.add(t);
                }
            }
            model.addAttribute("searched", true);
            model.addAttribute("trades", searchResults);
            model.addAttribute("page", searchResults);
        }else{
            Page<Trade> page = toPage(trades, pageLimit, pageNo);
            model.addAttribute("searched", false);
            model.addAttribute("trades", trades);
            model.addAttribute("page", page);
        }

        if (authentication != null && authentication.isAuthenticated()) {

            User currentUser = (User) authentication.getPrincipal();
            User fixedUser = userDAO.findByUsername(currentUser.getUsername());

            List<String> names = new ArrayList<>();

            for (Politician p : fixedUser.getPoliticians()) {
                names.add(p.getName());
            }
            model.addAttribute("names", names);
        }
        model.addAttribute("curPage",pageNo);

        return "trades";

    }

    @PostMapping("/trades")
    public String addFavorite(@RequestParam("ticker") String ticker, @RequestParam("pol_id") String name,@RequestParam("follow-btn") boolean follow, @RequestParam("search") boolean didSearch, @RequestParam("thisPage") String thisPage) {
//        System.out.printf("page value is %d%n",pageNo);
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

        String out=String.format("redirect:/trades?page=%s", thisPage);
        if(didSearch){
            out=String.format("redirect:/trades?searchbar=%s", ticker);
        }

        return out;

    }

//    @PostMapping("/trades/page")
//    public String changePage(@RequestParam("ticker") String ticker, @RequestParam("pol_id") String name,@RequestParam("follow-btn") boolean follow, @RequestParam(required = false,value="page-change",defaultValue = "0") int pageNo) {
//        System.out.printf("page value is %d%n",pageNo);
//        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User fixedUser = userDAO.findById(currentUser.getId()).get();
//        boolean isPresent = false;
//        if(follow){
//            for(Politician p : fixedUser.getPoliticians()){
//                if(p.getName().equals(name)){
//                    isPresent = true;
//                    break;
//                }
//            }
//            if(!isPresent){
//                fixedUser.addPolitician(politicianDAO.findByName(name));
//                userDAO.save(fixedUser);
//            }
//        }else{
//            fixedUser.removePolitician(politicianDAO.findByName(name));
//            userDAO.save(fixedUser);
//        }
//
//        String out=String.format("redirect:/trades?searchbar=%s&page=%n", ticker,pageNo);
//
//        return out;
//
//    }

}