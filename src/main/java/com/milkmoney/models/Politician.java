package com.milkmoney.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name ="politicians")
public class Politician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
    }
}
