package com.stock_recommendation.service;

import com.stock_recommendation.entity.PortfolioStock;
import com.stock_recommendation.entity.Stock;

import java.util.*;
import java.util.stream.Collectors;

public class PortfolioAnalyzerService {
    private final StockService stockService;
    private final PortfolioService portfolioService;

    public PortfolioAnalyzerService(StockService stockService, PortfolioService portfolioService) {
        this.stockService = stockService;
        this.portfolioService = portfolioService;
    }

    public List<String> getMissingIndustries(){
        Set<String> allAvailableIndustries = stockService.getAllStocks().stream()
                .map(Stock::getIndustry)
                .collect(Collectors.toSet());

        Set<String> portfolioIndustries = portfolioService.getPortfolio().stream()
                .map(portfolioStock -> portfolioStock.getStock())
                .map(Stock::getIndustry)
                .collect(Collectors.toSet());

        allAvailableIndustries.removeAll(portfolioIndustries);
        return new ArrayList<>(allAvailableIndustries);
    }
    public Map<String, List<Stock>> recommendMissingIndustriesStocks(){
        List<String> missingIndustries = this.getMissingIndustries();
        Map<String, List<Stock>> recommendations = new HashMap<>();

        for(String industry : missingIndustries){
            List<Stock> stocksFromIndustry = stockService.getStocksByIndustry(industry);
            if(!stocksFromIndustry.isEmpty()){
                recommendations.put(industry, stocksFromIndustry);
            }
        }

        return recommendations;
    }
    public Map<String, Integer> getIndustryQuantitiesForCurrentPortfolio(){
        List<PortfolioStock> portfolio = portfolioService.getPortfolio();
        Map<String, Integer> industryQuantities = new HashMap<>();

        for(PortfolioStock portfolioStock : portfolio){
            String industry = portfolioStock.getStock().getIndustry();
            int quantity = portfolioStock.getQuantity();

            industryQuantities.put(industry, industryQuantities.getOrDefault(industry, 0) + quantity);
        }
        return industryQuantities;
    }
    public List<String> getOverweightedIndustries(double thresholdPercentage){
        Map<String, Integer> industryQuantities = getIndustryQuantitiesForCurrentPortfolio();
        int totalQuantity = industryQuantities.values().stream().reduce(0, (a, b) -> a + b);

        List<String> overweighted = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : industryQuantities.entrySet()){
            double percentage = (entry.getValue() * 100.00) / totalQuantity;
            if(percentage > thresholdPercentage){
                overweighted.add(entry.getKey());
            }
        }
        return overweighted;
    }
    public List<String> getUnderweightedIndustries(double thresholdPercentage) {
        Map<String, Integer> industryQuantities = getIndustryQuantitiesForCurrentPortfolio();
        int totalQuantity = industryQuantities.values().stream().mapToInt(Integer::intValue).sum();

        List<String> underweighted = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : industryQuantities.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalQuantity;
            if (percentage < thresholdPercentage) {
                underweighted.add(entry.getKey());
            }
        }
        return underweighted;
    }
    public Map<String, List<Stock>> recommendToBalanceByQuantity(double maxPercentagePerIndustry){
        List<String> overweightedIndustries = getOverweightedIndustries(maxPercentagePerIndustry);
        Set<String> portfolioIndustries = portfolioService.getPortfolio().stream()
                .map(p -> p.getStock().getIndustry())
                .collect(Collectors.toSet());

        Set<String> allIndustries = stockService.getAllStocks().stream()
                .map(Stock::getIndustry)
                .collect(Collectors.toSet());

        // Industries that are underweighted or missing
        allIndustries.removeAll(portfolioIndustries);
        allIndustries.addAll(getUnderweightedIndustries(maxPercentagePerIndustry));

        Map<String, List<Stock>> recommendations = new HashMap<>();
        for (String industry : allIndustries) {
            List<Stock> candidates = stockService.getStocksByIndustry(industry);
            if (!candidates.isEmpty()) {
                recommendations.put(industry, candidates);
            }
        }
        return recommendations;
    }
    public List<PortfolioStock> getOverconcentratedStocks(int maxQuantity) {
        return portfolioService.getPortfolio().stream()
                .filter(p -> p.getQuantity() > maxQuantity)
                .collect(Collectors.toList());
    }
    public List<PortfolioStock> getRecommendedBalancedPortfolio(
            double maxIndustryPercentage,
            int maxStockQuantity,
            int suggestedStockQuantity
    ) {
        List<PortfolioStock> currentPortfolio = portfolioService.getPortfolio();
        Map<String, Integer> industryTotals = getIndustryQuantitiesForCurrentPortfolio();
        int totalQuantity = industryTotals.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Integer> updatedIndustryTotals = new HashMap<>(industryTotals);
        Map<String, Integer> stockQuantities = new HashMap<>();

        List<PortfolioStock> adjustedPortfolio = new ArrayList<>();

        // cap quantities by stock and industry for current portfolio
        for (PortfolioStock ps : currentPortfolio) {
            Stock stock = ps.getStock();
            String industry = stock.getIndustry();
            int currentQty = ps.getQuantity();

            // cap stock quantity
            int cappedQty = Math.min(currentQty, maxStockQuantity);

            // industry cap
            int industryQtySoFar = updatedIndustryTotals.getOrDefault(industry, 0);
            double projectedPercentage = (industryQtySoFar * 100.0) / totalQuantity;

            if (projectedPercentage <= maxIndustryPercentage) {
                adjustedPortfolio.add(new PortfolioStock(stock, cappedQty));
                stockQuantities.put(stock.getTicker(), cappedQty);
                updatedIndustryTotals.put(industry, industryQtySoFar + cappedQty);
            }
        }

        Set<String> existingIndustries = updatedIndustryTotals.keySet();
        Set<String> allIndustries = stockService.getAllStocks().stream()
                .map(Stock::getIndustry)
                .collect(Collectors.toSet());

        allIndustries.removeAll(existingIndustries); // only truly missing

        for (String missingIndustry : allIndustries) {
            List<Stock> candidates = stockService.getStocksByIndustry(missingIndustry);
            if (!candidates.isEmpty()) {
                Stock chosen = candidates.get(new Random().nextInt(candidates.size()));
                adjustedPortfolio.add(new PortfolioStock(chosen, suggestedStockQuantity));
                updatedIndustryTotals.put(missingIndustry, suggestedStockQuantity);
            }
        }

        return adjustedPortfolio;
    }
    public List<PortfolioStock> buildBalancedPortfolioEvenly(int totalTargetQuantity, int stocksPerIndustry) {
        List<Stock> allStocks = stockService.getAllStocks();

        Map<String, List<Stock>> stocksByIndustry = allStocks.stream()
                .collect(Collectors.groupingBy(Stock::getIndustry));

        int industryCount = stocksByIndustry.keySet().size();
        int quantityPerIndustry = totalTargetQuantity / industryCount;
        int quantityPerStock = quantityPerIndustry / stocksPerIndustry;

        List<PortfolioStock> balanced = new ArrayList<>();

        for (Map.Entry<String, List<Stock>> entry : stocksByIndustry.entrySet()) {
            List<Stock> industryStocks = entry.getValue();
            Collections.shuffle(industryStocks); // add randomness

            int limit = Math.min(stocksPerIndustry, industryStocks.size());
            for (int i = 0; i < limit; i++) {
                balanced.add(new PortfolioStock(industryStocks.get(i), quantityPerStock));
            }
        }

        return balanced;
    }
}
