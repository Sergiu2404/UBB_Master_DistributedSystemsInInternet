package com.example.weather_preferences_app.services;


import com.example.weather_preferences_app.entities.Country;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class CountryService {
    @PersistenceContext(unitName = "PU_SQLServer")
    private EntityManager entityManager;

    public Country getById(Long id){
        return this.entityManager.find(Country.class, id);
    }

    public List<Country> getAll(){
        return this.entityManager.createQuery("select c from Country c", Country.class)
                .getResultList();
    }

    public List<Country> getCountriesByName(String name){
        return this.entityManager.createQuery("select c from Country c where c.name like %:name%", Country.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void save(Country country) {
        this.entityManager.persist(country);
    }

    public void remove(Country country){
        this.entityManager.remove(country);
    }

    public void update(Country country, String name, String region){
        this.entityManager.merge(country);
    }
}

