package com.congresscashcow.congresscashhow.models;

import java.util.ArrayList;
import java.util.List;

public class Politician {
    private long id;
    private String polName;

    private List<Trade> trades = new ArrayList<>();
//    private String stockTicker;
//    private String transType;
//    private String rangeAmount;

    public Politician() {
    }

    public Politician(String polName, List<Trade> trades) {
        this.polName = polName;
        this.trades = trades;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
    }
}
