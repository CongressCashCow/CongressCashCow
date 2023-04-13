package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public void savePols(){
        politicianDAO.saveAll(api.getPoliticians());
    }

public static void main(String[] args) {

}

}
