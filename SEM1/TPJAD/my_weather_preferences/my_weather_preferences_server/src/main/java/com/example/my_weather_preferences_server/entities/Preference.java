package com.example.my_weather_preferences_server.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "preferences")
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    private double minTemperature;

    @Column
    private double maxTemperature;

    private Long locationId;

    public Preference(){}
    public Preference(String description, double minTemperature, double maxTemperature, Long locationId){
        this.locationId = locationId;
        this.description = description;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Long getLocation() {
        return locationId;
    }

    public void setLocation(Long locId) {
        this.locationId = locId;
    }
}
