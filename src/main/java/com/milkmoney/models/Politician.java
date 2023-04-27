package com.milkmoney.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name ="politicians")
public class Politician {

//    @Id
//    @Column
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

    @Id
    private String name;

    @Column
    private String imageURL;

    @ManyToMany(mappedBy = "politicians")
    private List<User> users;

    @Transient
    private List<Trade> trades = new ArrayList<>();

    public Politician() {
    }

    public Politician(String name, List<Trade> trades) {
        this.name = name;
        this.trades = trades;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public Trade getLatestTrade(){
        if(trades.size() > 0) {
            return trades.get(0);
        }
        return null;
        }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
    }

    public int getTradeCount(){
        return trades.size();
    }

    public long[] getTotals(){
        long[] output = new long[4];
        long stotalLow = 0;
        long stotalHigh = 0;
        long ptotalLow = 0;
        long ptotalHigh = 0;
        for(Trade t : trades){
//            System.out.println(t.getRange());
            if(t.getTransactionType().equals("Sale")) {
                if(t.getRange().contains("-")) {
                    String[] arr = t.getRange().replace(" ", "").replace(",", "").replace("$", "").split("-", 2);
                    stotalLow += Long.parseLong(arr[0]);
                    stotalHigh += Long.parseLong(arr[1]);
                }else{
                    System.out.println(Long.parseLong(t.getRange().replace(" ", "").replace(",", "").replace("$", "").replace("+", "").replace(">", "")));
                    stotalLow += Long.parseLong(t.getRange().replace(" ", "").replace(",", "").replace("$", "").replace("+", "").replace(">", ""));
                    stotalHigh += Long.parseLong(t.getRange().replace(" ", "").replace(",", "").replace("$", "").replace("+", "").replace(">", ""));
                }
            } else if (t.getTransactionType().equals("Purchase")) {
                if(t.getRange().contains("-")) {
                    String[] arr = t.getRange().replace(" ", "").replace(",", "").replace("$", "").split("-", 2);
                    ptotalLow += Long.parseLong(arr[0]);
                    ptotalHigh += Long.parseLong(arr[1]);
                }else{
                    ptotalLow += Math.floor(Long.parseLong(t.getRange().replace(" ", "").replace(",", "").replace("$", "").replace("+", "").replace(">", "")));
                    ptotalHigh += Math.floor(Long.parseLong(t.getRange().replace(" ", "").replace(",", "").replace("$", "").replace("+", "").replace(">", "")));

                }
            }
        }
        output[0] = stotalLow;
        output[1] = stotalHigh;
        output[2] = ptotalLow;
        output[3] = ptotalHigh;

        return output;
    }

}
