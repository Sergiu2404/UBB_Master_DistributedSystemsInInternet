package com.stock_recommendation.service;

import com.stock_recommendation.entity.PortfolioStock;
import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.repository.PortfolioRepository;
import com.stock_recommendation.repository.StockRepository;

import java.util.List;
import java.util.Optional;

public class PortfolioService {
    private PortfolioRepository portfolioRepository;
    private StockService stockService;

    public PortfolioService(PortfolioRepository portfolioRepository, StockService stockService){
        this.portfolioRepository = portfolioRepository;
        this.stockService = stockService;
    }

    public void addStock(String ticker, int quantity){
        Stock stock = this.stockService.getStockByTicker(ticker);
        this.portfolioRepository.addStock(stock, quantity);
    }

    public void removeStock(String ticker){
        this.portfolioRepository.removeStock(ticker);
    }

    public void updateStock(String ticker, int newQuantity){
        this.portfolioRepository.updateStock(ticker, newQuantity);
    }

    public List<PortfolioStock> getPortfolio(){ return this.portfolioRepository.getPortfolioStocks(); }
}
