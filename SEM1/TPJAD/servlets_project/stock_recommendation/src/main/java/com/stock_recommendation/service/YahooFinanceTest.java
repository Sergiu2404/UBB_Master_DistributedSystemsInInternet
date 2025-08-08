package com.stock_recommendation.service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class YahooFinanceTest {
    public static void main(String[] args) {
        System.out.println("Testing Yahoo Finance API...");

        String[] symbols = {"TSLA", "AAPL", "GOOGL", "MSFT"};

        for (String symbol : symbols) {
            try {
                System.out.println("\n--- Testing " + symbol + " ---");

                Stock stock = YahooFinance.get(symbol);

                if (stock == null) {
                    System.out.println("ERROR: Stock object is null for " + symbol);
                    continue;
                }

                System.out.println("Stock object created successfully");
                System.out.println("Name: " + stock.getName());
                System.out.println("Currency: " + stock.getCurrency());
                System.out.println("Exchange: " + stock.getStockExchange());

                if (stock.getQuote() == null) {
                    System.out.println("ERROR: Quote is null for " + symbol);
                    continue;
                }

                System.out.println("Quote object exists");
                System.out.println("Price: " + stock.getQuote().getPrice());
                System.out.println("Previous Close: " + stock.getQuote().getPreviousClose());
                System.out.println("Change: " + stock.getQuote().getChange());
                System.out.println("Change Percent: " + stock.getQuote().getChangeInPercent());
                System.out.println("Volume: " + stock.getQuote().getVolume());

                if (stock.getStats() != null) {
                    try {
                        System.out.println("Market Cap: " + stock.getStats().getMarketCap());
                    } catch (Exception e) {
                        System.out.println("Market Cap error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Stats is null");
                }

                System.out.println("SUCCESS: " + symbol + " data retrieved successfully");

            } catch (Exception e) {
                System.out.println("ERROR for " + symbol + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}