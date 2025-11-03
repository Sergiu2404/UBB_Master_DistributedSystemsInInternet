package com.example.server.services;

import com.example.server.config.JPAInitializer;
import com.example.server.entities.Location;
import jakarta.persistence.EntityManager;

import java.util.List;

public class LocationService {
    private final EntityManager entityManager;

    public LocationService(){
        this.entityManager = JPAInitializer.getEmfOracle().createEntityManager();
    }

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
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(location);
        this.entityManager.getTransaction().commit();
    }

    public void remove(Location location){
        this.entityManager.getTransaction().begin();
        this.entityManager.remove(location);
        this.entityManager.getTransaction().commit();
    }

    public void update(Location location){
        this.entityManager.getTransaction().begin();
        this.entityManager.merge(location);
        this.entityManager.getTransaction().commit();
    }
}
