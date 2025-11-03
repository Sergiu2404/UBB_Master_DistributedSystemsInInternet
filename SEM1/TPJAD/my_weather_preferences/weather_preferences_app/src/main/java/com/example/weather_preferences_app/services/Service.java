package com.example.weather_preferences_app.services;


import com.example.weather_preferences_app.entities.Country;
import com.example.weather_preferences_app.entities.Location;
import com.example.weather_preferences_app.entities.Preference;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import oracle.jdbc.proxy.annotation.Pre;

import java.util.List;

@Stateless
public class Service {
    @EJB
    private PreferenceService preferenceService;
    @EJB
    private CountryService countryService;
    @EJB
    private LocationService locationService;

//    public Service(CountryService countryService, LocationService locationService, PreferenceService preferenceService){
//        this.countryService = countryService;
//        this.locationService = locationService;
//        this.preferenceService = preferenceService;
//    }

    // country
    public List<Country> getAllCountries(){
        return this.countryService.getAll();
    }

    public Country getCountryById(Long id){
        return this.countryService.getById(id);
    }
    public void saveCountry(Country country) {
        countryService.save(country);
    }

    public void updateCountry(Country country, String name, String region) {
        countryService.update(country, name, region);
    }

    public void removeCountry(Country country) {
        List<Location> locations = locationService.getLocationsByCountryId(country.getId());
        for (Location loc : locations) {
            List<Preference> prefs = preferenceService.getPreferencesByLocationId(loc.getId());
            for (Preference p : prefs) {
                preferenceService.remove(p);
            }
            locationService.remove(loc);
        }
        countryService.remove(country);
    }

    //locations
    public List<Location> getAllLocations(){
        return this.locationService.getAll();
    }

    public Location getLocationById(Long id) {
        return locationService.getById(id);
    }

    public void saveLocation(Location location) {
        locationService.save(location);
    }

    public void updateLocation(Location location) {
        locationService.update(location);
    }

    public void removeLocation(Location location) {
        List<Preference> prefs = preferenceService.getPreferencesByLocationId(location.getId());
        for (Preference p : prefs) {
            preferenceService.remove(p);
        }
        locationService.remove(location);
    }

    //preferences
    public List<Preference> getAllPreferences(){
        return this.preferenceService.getAll();
    }

    public Preference getPreferenceById(Long id) {
        return preferenceService.getById(id);
    }

    public List<Preference> getPreferencesByLocationId(Long id){
        return this.preferenceService.getPreferencesByLocationId(id);
    }

    public void savePreference(Preference preference) {
        preferenceService.save(preference);
    }

    public void updatePreference(Preference preference) {
        preferenceService.update(preference);
    }

    public void removePreference(Preference preference) {
        preferenceService.remove(preference);
    }
}
