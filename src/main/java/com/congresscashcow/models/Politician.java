package com.congresscashcow.models;

public class Politician {
    private long id;
    private String polName;
    private String stockTicker;
    private String transType;
    private String rangeAmount;

    public Politician() {
    }

    public Politician(long id, String polName, String stockTicker) {
        this.id = id;
        this.polName = polName;
        this.stockTicker = stockTicker;
    }

    public Politician(long id, String polName, String stockTicker, String transType, String rangeAmount) {
        this.id = id;
        this.polName = polName;
        this.stockTicker = stockTicker;
        this.transType = transType;
        this.rangeAmount = rangeAmount;
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

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getRangeAmount() {
        return rangeAmount;
    }

    public void setRangeAmount(String rangeAmount) {
        this.rangeAmount = rangeAmount;
    }
}
