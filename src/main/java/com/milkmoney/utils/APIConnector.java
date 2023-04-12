package com.milkmoney.utils;

import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class APIConnector {
    private Date updatedDate = new Date();
    static APIConnector api = new APIConnector();
    private  Set<Politician> politicians = new HashSet<Politician>();
    private  Set<Trade> trades = new HashSet<Trade>();
    private  HashMap<Long, String> politicianIdKeeper = new HashMap<Long, String>(); //sims db; rm after db
    private static String getData(){
        HttpResponse<String> response = Unirest.get("https://api.quiverquant.com/beta/bulk/congresstrading")
                .header("accept", "application/json")
                .header("X-CSRFToken", "TyTJwjuEC7VV7mOqZ622haRaaUr0x0Ng4nrwSRFKQs7vdoBcJlK9qjAS69ghzhFu")
                .header("Authorization", "Token 82af78c50f344c3615a406803c22430c314dc7df")
                .asString();

        return response.getBody();
    }
     int r = 0;
    private  void verification() throws InterruptedException {
        while (r < 100) {
            System.out.println("running");
            TimeUnit.SECONDS.sleep(10);

            System.out.println(politicianIdKeeper.get(200L));
            System.out.println(politicians.size());
            System.out.println(trades.size());
            r++;
        }
    }
int k = 0;
    private  void rerunner() throws InterruptedException {
        while (k < 100) {
            System.out.println("calling");
            TimeUnit.SECONDS.sleep(32);

            api.formatData();
            k++;
        }
    }


    private  void formatData(){

        ObjectMapper mapper = new ObjectMapper();
        try {

            ArrayList<Object> unformattedObjs = mapper.readValue(getData(),
                    new TypeReference<ArrayList<Object>>(){});

//            List<String> polNames = new ArrayList<>();
//            List<Politician> pols = new ArrayList<>();
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
//            System.out.println(politicians.size());
//            for(Politician pol : pols){
//                counter+=pol.getTrades().size();
//            }

//            System.out.println(unformattedObjs.toString().length());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<Politician> getPoliticians() {
        return politicians;
    }

    public void setPoliticians(Set<Politician> politicians) {
        this.politicians = politicians;
    }

    public void addPolitician(Politician politician){
        this.politicians.add(politician);
    }

    public Set<Trade> getTrades() {
        return trades;
    }

    public void setTrades(Set<Trade> trades) {
        this.trades = trades;
    }

    public static void main(String[] args) {
        try {

            api.formatData();
            api.verification();
            api.rerunner();
//            verification();
        }
        catch (Exception e ){
            System.out.println(e);
        }
//    formatData();

    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate() {
        this.updatedDate = new Date();
    }
}