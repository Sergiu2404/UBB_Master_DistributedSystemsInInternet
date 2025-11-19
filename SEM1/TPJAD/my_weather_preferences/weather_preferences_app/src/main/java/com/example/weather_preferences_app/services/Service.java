package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Country;
import com.example.weather_preferences_app.entities.Location;
import com.example.weather_preferences_app.entities.Preference;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Objects;

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
            if (countryService.existsByName(country.getName())) {
                System.out.println("Country with this name already exists: " + country.getName());
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
            if(countryService.existsByName(name)){
                System.out.println("Cannot rename — country with this name already exists: " + name);
                return false;
            }

            countryService.update(country);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCountry(Long countryId) {
        try {
            List<Location> locations = locationService.getLocationsByCountryId(countryId);

            for (Location loc : locations) {
                preferenceService.removePreferencesByLocationId(loc.getId());
            }

            locationService.removeLocationsByCountryId(countryId);

            countryService.removeCountryById(countryId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Location> getAllLocations() {
        return locationService.getAll();
    }

    public List<Location> getLocationsByCountryId(Long countryId) {
        return locationService.getLocationsByCountryId(countryId);
    }

    public Location getLocationById(Long id) {
        return locationService.getById(id);
    }

    public boolean saveLocation(Location location) {
        try {
            if (location == null || location.getName() == null || location.getName().isBlank()) {
                return false;
            }
            if (locationService.existsByName(location.getName())) {
                System.out.println("Location with this name already exists: " + location.getName());
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

    public boolean removeLocation(Long locationId) {
        try {
            this.preferenceService.removePreferencesByLocationId(locationId);

            this.locationService.removeLocationById(locationId);

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
            if (preference == null || preference.getDescription() == null || Objects.equals(preference.getDescription(), "")) return false;
            if (preferenceService.existsByDescription(preference.getDescription())) {
                System.out.println("Preference with this description already exists: " + preference.getDescription());
                return false;
            }

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

    public boolean existsPreferenceByDescription(String description){
        return this.preferenceService.existsByDescription(description);
    }
    public boolean existsCountryByName(String name){
        return this.countryService.existsByName(name);
    }
    public boolean existsLocationByName(String name){
        return this.locationService.existsByName(name);
    }

    public boolean removePreference(Long preferenceId) {
        try {
            preferenceService.removePreferenceById(preferenceId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
