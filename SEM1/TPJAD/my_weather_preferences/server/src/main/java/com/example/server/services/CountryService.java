package com.example.server.services;

import com.example.server.config.JPAInitializer;
import com.example.server.entities.Country;
import jakarta.persistence.EntityManager;

public class CountryService {
    private EntityManager em;

    public CountryService() {
        em = JPAInitializer.getEmfSQLServer().createEntityManager();
    }

    public void save(Country country) {
        em.getTransaction().begin();
        em.persist(country);
        em.getTransaction().commit();
    }
}

