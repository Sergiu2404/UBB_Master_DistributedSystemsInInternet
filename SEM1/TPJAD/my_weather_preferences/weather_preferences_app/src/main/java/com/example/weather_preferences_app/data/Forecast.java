package com.example.weather_preferences_app.data;

public class Forecast {
    private String date;
    private double maxTemp;
    private double minTemp;
    public Forecast(String date, double minTemp, double maxTemp){
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDate() {
        return date;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }
}
