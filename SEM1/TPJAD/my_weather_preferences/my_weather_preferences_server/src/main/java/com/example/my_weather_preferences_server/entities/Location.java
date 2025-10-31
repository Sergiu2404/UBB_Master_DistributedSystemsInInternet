package com.example.my_weather_preferences_server.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private double latitude;
    @Column
    private double longitude;

    // many locations belong to one user
    private Long countryId;

    public Location() {}

    public Location(String name, double latitude, double longitude, Long countryId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryId = countryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getCountry() {
        return countryId;
    }

    public void setCountry(Long countryId) {
        this.countryId = countryId;
    }
}