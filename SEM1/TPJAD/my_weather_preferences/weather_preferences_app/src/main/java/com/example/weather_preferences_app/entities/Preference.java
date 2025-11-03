package com.example.weather_preferences_app.entities;

import jakarta.persistence.*;

@Entity(name="Preference")
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

    public Preference(String description, double minTemperature, double maxTemperature, Long locationId) {
        this.description = description;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.locationId = locationId;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
