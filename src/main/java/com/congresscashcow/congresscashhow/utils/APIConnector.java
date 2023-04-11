package com.congresscashcow.congresscashhow.utils;

import com.congresscashcow.congresscashhow.models.Politician;
import com.congresscashcow.congresscashhow.models.Trade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import java.io.IOException;
import java.util.*;

public class APIConnector {
    private static String getData(){
        HttpResponse<String> response = Unirest.get("https://api.quiverquant.com/beta/bulk/congresstrading")
                .header("accept", "application/json")
                .header("X-CSRFToken", "TyTJwjuEC7VV7mOqZ622haRaaUr0x0Ng4nrwSRFKQs7vdoBcJlK9qjAS69ghzhFu")
                .header("Authorization", "Token 82af78c50f344c3615a406803c22430c314dc7df")
                .asString();
        return response.getBody();
    }
static int counter=0;
    private static void formatData(){

        ObjectMapper mapper = new ObjectMapper();
        try {

            ArrayList<Object> unformattedObjs = mapper.readValue(getData(),
                    new TypeReference<ArrayList<Object>>(){});

            List<String> polNames = new ArrayList<>();
            List<Politician> pols = new ArrayList<>();
            long id = 0;
            long tradeId = 0;
            for(Object obj : unformattedObjs){
//                System.out.println(obj.toString());
                String ticker = obj.toString().substring(obj.toString().indexOf("Ticker=") + 7,obj.toString().indexOf(", Rep"));
                String rep = obj.toString().substring(obj.toString().indexOf("Representative=") + 16,obj.toString().indexOf(", Transaction="));
                String date = obj.toString().substring(obj.toString().indexOf("nDate=") + 6,obj.toString().indexOf(", Tic"));
                String type = obj.toString().substring(obj.toString().indexOf("Transaction=") + 12,obj.toString().indexOf(", Am"));
                String range = obj.toString().substring(obj.toString().indexOf("Range=") + 6,obj.toString().indexOf("}"));
                Politician p = null;
                if(!polNames.contains(rep)){
                    p = new Politician();
                    p.setId(id);
                    p.setPolName(rep);
                    pols.add(p);
                    polNames.add(rep);
                    id++;
                }else{
                    for(Politician pol : pols){
                        if(pol.getPolName().equals(rep)){
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
                p.addTrade(trade);
                tradeId++;
            }
            System.out.println(pols.size());
//            for(Politician pol : pols){
//                counter+=pol.getTrades().size();
//            }
            System.out.println(counter);
//            System.out.println(unformattedObjs.toString().length());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    formatData();

    }

}