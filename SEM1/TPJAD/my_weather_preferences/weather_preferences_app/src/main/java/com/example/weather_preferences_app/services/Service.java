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
            List<Location> locations = locationService.getLocationsByCountryId(countryId);

            // del pref for each location: separate transaction to avoid detached entity exception (JPA cant delete objects from diff persistence contexts)
            for (Location loc : locations) {
                preferenceService.removePreferencesByLocationId(loc.getId());
            }

            // del locations for this country (separate transaction)
            locationService.removeLocationsByCountryId(countryId);

            // del country (separate transaction)
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
            List<Preference> preferences = this.preferenceService.getPreferencesByLocationId(locationId);

            // del pref for each location: separate transaction to avoid detached entity exception (JPA cant delete objects from diff persistence contexts)
            this.preferenceService.removePreferencesByLocationId(locationId);

            // del country (separate transaction)
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

//    public boolean removePreference(Preference preference) {
//        try {
//            if (preference == null) return false;
//            preferenceService.remove(preference);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
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
