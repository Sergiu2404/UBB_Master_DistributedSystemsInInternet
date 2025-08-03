package com.stock_recommendation.entity;

public class Stock {
    private String ticker;
    private String companyName;
    private String industry;
    private String description;
    private String website;

    public Stock(){}
    public Stock(String ticker, String companyName, String industry, String description, String website){
        this.ticker = ticker;
        this.companyName = companyName;
        this.industry = industry;
        this.description = description;
        this.website = website;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
