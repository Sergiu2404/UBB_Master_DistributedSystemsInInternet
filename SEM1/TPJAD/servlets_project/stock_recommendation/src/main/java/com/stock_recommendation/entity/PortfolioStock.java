package com.stock_recommendation.entity;

public class PortfolioStock {
    private Stock stock;
    private int quantity;

    public PortfolioStock(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
