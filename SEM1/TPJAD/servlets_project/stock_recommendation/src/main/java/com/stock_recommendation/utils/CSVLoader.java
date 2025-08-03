package com.stock_recommendation.utils;

import com.stock_recommendation.entity.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    private static final String CSV_BVB_STOCKS_INFO = "bvb_info_enhanced.csv";
    private List<Stock> stocks;

    public List<Stock> getAllStocks(){
        return this.stocks;
    }

    public Stock findByTicker(String ticker){
        return stocks.stream()
                .filter(s -> s.getTicker().equals(ticker))
                .findFirst()
                .orElse(null);
    }

    private List<String> parseCSVLine(String line){
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for(char c : line.toCharArray()){
            if(c == '"'){
                inQuotes = !inQuotes;
            }
            else if(c == ',' && !inQuotes){
                result.add(current.toString());
                current.setLength(0);
            }
            else{
                current.append(c);
            }
        }

        result.add(current.toString());
        return result;
    }

    public List<Stock> loadEnhancedStocksInfoFromCSV(){
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_BVB_STOCKS_INFO);
            if(inputStream == null){
                throw new RuntimeException("File " + CSV_BVB_STOCKS_INFO + " not found");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                String line;
                boolean firstLine = true; // first line is header
                while((line = reader.readLine()) != null){
                    if(firstLine){
                        firstLine = false;
                        continue;
                    }

                    List<String> fields = parseCSVLine(line);
                    if(fields.size() == 5){
                        stocks.add(
                                new Stock(
                                        fields.get(0).trim(),
                                        fields.get(1).trim(),
                                        fields.get(2).trim(),
                                        fields.get(3).trim(),
                                        fields.get(4).trim()
                                )
                        );
                    }
                }
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }
}
