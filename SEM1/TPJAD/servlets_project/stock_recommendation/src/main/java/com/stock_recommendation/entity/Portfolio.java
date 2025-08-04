package com.stock_recommendation.entity;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private List<PortfolioStock> portfolio;

    public Portfolio(){
        this.portfolio = new ArrayList<>();
    }

    public void addStock(Stock stock, int quantity){
        portfolio.add(new PortfolioStock(stock, quantity));
    }

    public void removeStock(String ticker){
        portfolio.removeIf(portfolioStock -> portfolioStock.getStock().getTicker().equals(ticker));
    }

    public void updateStock(String ticker, int newQuantity){
        portfolio.stream()
                .filter(stock -> stock.getStock().getTicker().equals(ticker))
                .findFirst()
                .ifPresent(
                        stock -> stock.setQuantity(newQuantity)
                );
    }

    public List<PortfolioStock> getPortfolioStocks(){ return this.portfolio; }
}
