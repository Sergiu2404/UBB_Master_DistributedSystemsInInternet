package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Location;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;

@Stateless
public class LocationService {
    @PersistenceContext(unitName = "PU_Postgres")
    private EntityManager entityManager;

    public List<Location> getAll(){
        return this.entityManager.createQuery("select l from Location l", Location.class)
                .getResultList();
    }

    public Location getById(Long id){
        return this.entityManager.find(Location.class, id);
    }

    public List<Location> getLocationsByCountryId(Long countryId) {
        return entityManager.createQuery("SELECT l FROM Location l WHERE l.countryId = :countryId", Location.class)
                .setParameter("countryId", countryId)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeLocationsByCountryId(Long countryId) {
        entityManager.createQuery("DELETE FROM Location l WHERE l.countryId = :countryId")
                .setParameter("countryId", countryId)
                .executeUpdate();
        entityManager.flush();
    }

    public void save(Location location){
        this.entityManager.persist(location);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeLocationById(Long locationId) {
        this.entityManager.createQuery("DELETE FROM Location l WHERE l.id = :locationId")
                .setParameter("locationId", locationId)
                .executeUpdate();
        this.entityManager.flush(); // force immediate execution to ensure is made in this transaction
    }

    public void update(Location location){
        this.entityManager.merge(location);
    }
}
