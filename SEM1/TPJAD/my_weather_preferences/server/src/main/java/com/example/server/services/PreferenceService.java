package com.example.server.services;

import com.example.server.config.JPAInitializer;
import com.example.server.entities.Preference;
import jakarta.persistence.EntityManager;

public class PreferenceService {
    private EntityManager em;

    public PreferenceService() {
        em = JPAInitializer.getEmfPostgres().createEntityManager();
    }

    public void save(Preference pref) {
        em.getTransaction().begin();
        em.persist(pref);
        em.getTransaction().commit();
    }
}
