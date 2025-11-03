package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Location;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class LocationService {
    @PersistenceContext(unitName = "PU_Oracle")
    private EntityManager entityManager;

    public List<Location> getAll(){
        return this.entityManager.createQuery("select l from Location l", Location.class)
                .getResultList();
    }

    public Location getById(Long id){
        return this.entityManager.find(Location.class, id);
    }

    public List<Location> getLocationsByCountryId(Long countryId){
        return this.entityManager.createQuery("select l from Location l where l.countryId = :countryId", Location.class)
                .setParameter("countryId", countryId)
                .getResultList();
    }

    public void save(Location location){
        this.entityManager.persist(location);
    }

    public void remove(Location location){
        this.entityManager.remove(location);
    }

    public void update(Location location){
        this.entityManager.merge(location);
    }
}
