package com.stock_recommendation.service;

import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.repository.StockRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public List<Stock> getStocksByIndustry(String industry){
        List<Stock> stocks = stockRepository.getAllStocks();
        return stocks.stream()
                .filter(stock -> stock.getIndustry().equals(industry))
                .collect(Collectors.toList());
    }
    public Stock getStockByTicker(String ticker){
        List<Stock> stocks = stockRepository.getAllStocks();
        return stocks.stream()
                .filter(stock -> stock.getTicker().equals(ticker))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Element with ticker " + ticker + " not found"));
    }
    public List<Stock> getAllStocks(){ return this.stockRepository.getAllStocks(); }
}
