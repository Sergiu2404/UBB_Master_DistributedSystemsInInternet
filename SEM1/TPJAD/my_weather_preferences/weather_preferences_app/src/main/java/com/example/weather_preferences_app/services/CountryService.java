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

//    public CountryService() {
//        this.entityManager = JPAInitializer.getEmfSQLServer().createEntityManager();
//    }

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
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(country);
        this.entityManager.getTransaction().commit();
    }

    public void remove(Country country){
        this.entityManager.getTransaction().begin();
        this.entityManager.remove(country);
        this.entityManager.getTransaction().commit();
    }

    public void update(Country country, String name, String region){
        this.entityManager.getTransaction().begin();
        this.entityManager.merge(country);
        this.entityManager.getTransaction().commit();
    }
}

