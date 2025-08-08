//package com.stock_recommendation.service;
//
//import com.stock_recommendation.entity.PortfolioStock;
//import com.stock_recommendation.entity.Stock;
//import com.stock_recommendation.repository.PortfolioRepository;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class PortfolioService {
//    private PortfolioRepository portfolioRepository;
//    private StockService stockService;
//
//    public PortfolioService(PortfolioRepository portfolioRepository, StockService stockService){
//        this.portfolioRepository = portfolioRepository;
//        this.stockService = stockService;
//    }
//
//    public void addStock(String ticker, int quantity){
//        Stock stock = this.stockService.getStockByTicker(ticker);
//        this.portfolioRepository.addStock(stock, quantity);
//    }
//
//    public void removeStock(String ticker){
//        this.portfolioRepository.removeStock(ticker);
//    }
//
//    public void updateStock(String ticker, int newQuantity){
//        this.portfolioRepository.updateStock(ticker, newQuantity);
//    }
//
//    public List<PortfolioStock> getPortfolio(){ return this.portfolioRepository.getPortfolioStocks(); }
//
//    public List<Stock> getAvailableStocksNotInPortfolio() {
//        List<PortfolioStock> portfolio = getPortfolio();
//        Set<String> portfolioTickers = portfolio.stream()
//                .map(p -> p.getStock().getTicker())
//                .collect(Collectors.toSet());
//
//        return stockService.getAllStocks().stream()
//                .filter(stock -> !portfolioTickers.contains(stock.getTicker()))
//                .collect(Collectors.toList());
//    }
//
//}
