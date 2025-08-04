package com.stock_recommendation.repository;

import com.stock_recommendation.entity.PortfolioStock;
import com.stock_recommendation.entity.Stock;

import java.util.ArrayList;
import java.util.List;

public class PortfolioRepository {
    private List<PortfolioStock> portfolio;

    public PortfolioRepository(){
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
