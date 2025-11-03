package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Country;
import com.example.weather_preferences_app.entities.Location;
import com.example.weather_preferences_app.entities.Preference;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.util.List;

@Stateless
public class Service {
    @EJB
    private PreferenceService preferenceService;

    @EJB
    private CountryService countryService;

    @EJB
    private LocationService locationService;

    public List<Country> getAllCountries() {
        return countryService.getAll();
    }

    public Country getCountryById(Long id) {
        return countryService.getById(id);
    }

    public boolean saveCountry(Country country) {
        try {
            if (country == null || country.getName() == null || country.getRegion() == null
                    || country.getName().isBlank() || country.getRegion().isBlank()) {
                return false;
            }
            countryService.save(country);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCountry(Country country, String name, String region) {
        try {
            if (country == null) return false;
            if (name != null && !name.isBlank()) country.setName(name);
            if (region != null && !region.isBlank()) country.setRegion(region);

            countryService.update(country, name, region);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCountry(Long countryId) {
        try {
            // Step 1: Get locations first (read-only)
            List<Location> locations = locationService.getLocationsByCountryId(countryId);

            // Step 2: Delete preferences for each location (separate transactions)
            for (Location loc : locations) {
                preferenceService.deletePreferencesByLocationId(loc.getId());
            }

            // Step 3: Delete all locations for this country (separate transaction)
            locationService.deleteLocationsByCountryId(countryId);

            // Step 4: Delete country (separate transaction)
            countryService.deleteCountryById(countryId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<Location> getAllLocations() {
        return locationService.getAll();
    }

    public Location getLocationById(Long id) {
        return locationService.getById(id);
    }

    public boolean saveLocation(Location location) {
        try {
            if (location == null || location.getName() == null || location.getName().isBlank()) {
                return false;
            }
            locationService.save(location);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLocation(Location location) {
        try {
            if (location == null) return false;
            locationService.update(location);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeLocation(Location location) {
        try {
            if (location == null) return false;

            List<Preference> prefs = preferenceService.getPreferencesByLocationId(location.getId());
            for (Preference p : prefs) {
                preferenceService.remove(p);
            }
            locationService.remove(location);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Preference> getAllPreferences() {
        return preferenceService.getAll();
    }

    public Preference getPreferenceById(Long id) {
        return preferenceService.getById(id);
    }

    public List<Preference> getPreferencesByLocationId(Long id) {
        return preferenceService.getPreferencesByLocationId(id);
    }

    public boolean savePreference(Preference preference) {
        try {
            if (preference == null) return false;
            preferenceService.save(preference);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePreference(Preference preference) {
        try {
            if (preference == null) return false;
            preferenceService.update(preference);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePreference(Preference preference) {
        try {
            if (preference == null) return false;
            preferenceService.remove(preference);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
