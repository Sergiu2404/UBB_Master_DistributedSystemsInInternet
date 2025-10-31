package com.example.my_weather_preferences_server.services;

import com.example.my_weather_preferences_server.entities.Preference;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PostgresService {
    @PersistenceContext(unitName = "PU_Postgres")
    private EntityManager em;

    public void create(Preference p) { em.persist(p); }
    public Preference read(Long id) { return em.find(Preference.class, id); }
    public void update(Preference p) { em.merge(p); }
    public void delete(Long id) {
        Preference p = em.find(Preference.class, id);
        if (p != null) em.remove(p);
    }
}
