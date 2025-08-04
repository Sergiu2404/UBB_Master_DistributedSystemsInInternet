package com.stock_recommendation.repository;

import com.stock_recommendation.entity.Stock;
import com.stock_recommendation.utils.CSVLoader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StockRepository {
    private CSVLoader csvLoader;
    private List<Stock> stocks;

    public StockRepository(CSVLoader csvLoader){
        this.csvLoader = csvLoader;
        this.loadStockData();
    }

    private void loadStockData(){
        this.stocks = this.csvLoader.loadEnhancedStocksInfoFromCSV();
    }

    public List<Stock> getAllStocks(){ return this.stocks; }
}
