package com.milkmoney.controllers;

import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.models.Politician;
import com.milkmoney.utils.APIConnector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
public class PoliticianController {
    private final PoliticianRepository politicianDAO;

//    static APIConnector api = new APIConnector();
    public PoliticianController(PoliticianRepository politicianDAO) {

        this.politicianDAO = politicianDAO;
    }

    @GetMapping("/update")
    public String update(){

        APIConnector.update();
        APIConnector.updateImageURLs();
        savePols();
        return "api";
    }
    @GetMapping("/api")
    public String apiView(Model model) {
            Set<Politician> pols = APIConnector.getPoliticians();

            model.addAttribute("pols",pols);


        return "api";
    }
    public void savePols(){
        politicianDAO.saveAll(APIConnector.getPoliticians());
    }

public static void main(String[] args) {

}

}
