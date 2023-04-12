package com.milkmoney.utils;

import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class APIConnector {


    private static Date updatedDate = new Date();
    static APIConnector api = new APIConnector();
    private  static Set<Politician> politicians = new HashSet<Politician>();
    private  static Set<Trade> trades = new HashSet<Trade>();
    private static HashMap<Long, String> politicianIdKeeper = new HashMap<Long, String>(); //sims db; rm after db
    private static String getData(){
        HttpResponse<String> response = Unirest.get("https://api.quiverquant.com/beta/bulk/congresstrading")
                .header("accept", "application/json")
                .header("X-CSRFToken", "TyTJwjuEC7VV7mOqZ622haRaaUr0x0Ng4nrwSRFKQs7vdoBcJlK9qjAS69ghzhFu")
                .header("Authorization", "Token 82af78c50f344c3615a406803c22430c314dc7df")
                .asString();

        return response.getBody();
    }
     static int r = 0;
    private static void verification() throws InterruptedException {
        while (r < 100) {
            System.out.println("running");
            TimeUnit.SECONDS.sleep(10);

            System.out.println(politicianIdKeeper.get(200L));
            System.out.println(politicians.size());
            System.out.println(trades.size());
            r++;
        }
    }
    static int k = 0;
    private static void rerunner() throws InterruptedException {
        while (k < 100) {
            System.out.println("calling");
            TimeUnit.SECONDS.sleep(32);

            api.update();
            k++;
        }
    }


    public static void update(){
        trades.clear();

        ObjectMapper mapper = new ObjectMapper();
        try {

            ArrayList<Object> unformattedObjs = mapper.readValue(getData(),
                    new TypeReference<ArrayList<Object>>(){});

            long id = politicianIdKeeper.size();
            long tradeId = trades.size();
            for(Object obj : unformattedObjs){
                String ticker = obj.toString().substring(obj.toString().indexOf("Ticker=") + 7,obj.toString().indexOf(", Rep"));
                String rep = obj.toString().substring(obj.toString().indexOf("Representative=") + 15,obj.toString().indexOf(", Transaction="));
                String date = obj.toString().substring(obj.toString().indexOf("nDate=") + 6,obj.toString().indexOf(", Tic"));
                String type = obj.toString().substring(obj.toString().indexOf("Transaction=") + 12,obj.toString().indexOf(", Am"));
                String range = obj.toString().substring(obj.toString().indexOf("Range=") + 6,obj.toString().indexOf("}"));
                Politician p = null;
                if(!politicianIdKeeper.containsValue(rep)){
                    p = new Politician();
                    p.setId(id);
                    p.setName(rep);
                    politicians.add(p);
                    politicianIdKeeper.put(id,rep);

                    id++;
                }else{
                    for(Politician pol : politicians){
                        if(pol.getName().equals(rep)){
                            p = pol;
                        }
                    }
                }
                Trade trade = new Trade();
                trade.setId(tradeId);
                trade.setRange(range);
                trade.setTicker(ticker);
                trade.setDate(date);
                trade.setTransactionType(type);
                trade.setPolitician(p);
                trades.add(trade);
                p.addTrade(trade);
                tradeId++;
            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateImageURLs(){
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
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static Set<Politician> getPoliticians() {
        return politicians;
    }

    public static void setPoliticians(Set<Politician> politicianz) {
        politicians = politicianz;
    }

    public static void addPolitician(Politician politician){
        politicians.add(politician);
    }

    public static Set<Trade> getTrades() {
        return trades;
    }

    public static void setTrades(Set<Trade> tradez) {
        trades = tradez;
    }

//    public static void main(String[] args) {
//        try {
//            api.update();
//        }
//        catch (Exception e ){
//            System.out.println(e);
//        }
//    }

    public static Date getUpdatedDate() {
        return updatedDate;
    }

}