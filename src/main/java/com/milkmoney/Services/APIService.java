package com.milkmoney.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;

import com.milkmoney.utils.PolImg;
import jakarta.annotation.PostConstruct;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class APIService {
    @Value("${apiKey}")
    private String apiKey;
    @Autowired
    public APIService(){}
    private Date updatedDate = new Date();
    private Set<Politician> politicians = new HashSet<Politician>();
    private   List<Trade> trades = new ArrayList<>();

    private String getData(){
        HttpResponse<String> response = Unirest.get("https://api.quiverquant.com/beta/bulk/congresstrading")
                .header("accept", "application/json")
                .header("X-CSRFToken", "TyTJwjuEC7VV7mOqZ622haRaaUr0x0Ng4nrwSRFKQs7vdoBcJlK9qjAS69ghzhFu")
                .header("Authorization", String.format("Token %s", apiKey))
                .asString();

        return response.getBody();
    }

    @PostConstruct
    public  void update(){
        List<PolImg> imageList = getImageList();
        System.out.printf("LIST LENGTH %d  ", imageList.size());
        trades.clear();

        ObjectMapper mapper = new ObjectMapper();
        try {

            ArrayList<Object> unformattedObjs = mapper.readValue(getData(),
                    new TypeReference<ArrayList<Object>>(){});

            long tradeId = trades.size();

            for(Object obj : unformattedObjs){
                String ticker = obj.toString().substring(obj.toString().indexOf("Ticker=") + 7,obj.toString().indexOf(", Rep"));
                String rep = obj.toString().substring(obj.toString().indexOf("Representative=") + 15,obj.toString().indexOf(", Transaction="));
                String date = obj.toString().substring(obj.toString().indexOf("nDate=") + 6,obj.toString().indexOf(", Tic"));
                String type = obj.toString().substring(obj.toString().indexOf("Transaction=") + 12,obj.toString().indexOf(", Am"));
                String range = obj.toString().substring(obj.toString().indexOf("Range=") + 6,obj.toString().indexOf("}"));
                boolean isAdded = false;
                Politician p = null;


                for(Politician pol : politicians){
                    if(pol.getName().equals(rep)){
                        p = pol;
                        isAdded = true;
                    }
                }
                if(!isAdded){
                    p = new Politician();
                    p.setName(rep);
                    for(PolImg polimg : imageList){
                        if(polimg.getName().equals(rep.toLowerCase())){
                            p.setImageURL(polimg.getImage());
                            break;
                        }
                    }
                    politicians.add(p);
                }
                Trade trade = new Trade();
                trade.setId(tradeId);
                trade.setRange(range);
                trade.setTicker(ticker);
                LocalDate formattedDate = LocalDate.parse(date);
                trade.setDate(formattedDate);
                trade.setTransactionType(type);
                trade.setPolitician(p);
                trades.add(trade);
                p.addTrade(trade);
                tradeId++;
            }
//            updateImageURLs();
            System.out.printf("number : %d", politicians.size());
            sortTrades();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortTrades(){
        Collections.sort(trades);
        Collections.reverse(trades);
    }

    private List<PolImg> getImageList(){
        List<PolImg> listPol = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            listPol = mapper.readValue(new File("src/main/resources/static/images.json"), new TypeReference<List<PolImg>>(){});

        }
        catch (Exception e){
            System.out.println(e);
        }
        return listPol;
    }

    public  void updateImageURLs(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<PolImg> listPol = mapper.readValue(new File("src/main/resources/static/images.json"), new TypeReference<List<PolImg>>(){});

            for (PolImg pig : listPol) {

                for(Politician p : politicians){
                    if(p.getName().toLowerCase().equals(pig.getName())){
                        p.setImageURL(pig.getImage());
                    }
                }
            }
//            save
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public  Set<Politician> getPoliticians() {
        return politicians;
    }

    public  void setPoliticians(Set<Politician> politicianz) {
        politicians = politicianz;
    }

    public  void addPolitician(Politician politician){
        politicians.add(politician);
    }

    public  List<Trade> getTrades() {
        return trades;
    }

    public  void setTrades(List<Trade> tradez) {
        trades = tradez;
    }

    public  Date getUpdatedDate() {
        return updatedDate;
    }

}
