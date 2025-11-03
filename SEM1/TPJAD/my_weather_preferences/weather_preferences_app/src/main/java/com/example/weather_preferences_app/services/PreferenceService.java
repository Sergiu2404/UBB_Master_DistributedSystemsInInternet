package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Preference;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PreferenceService {
    @PersistenceContext(unitName = "PU_Postgres")
    private EntityManager entityManager;

//    public PreferenceService() {
//        this.entityManager = JPAInitializer.getEmfPostgres().createEntityManager();
//    }

    public List<Preference> getAll(){
        return this.entityManager.createQuery("select p from Preference p", Preference.class)
                .getResultList();
    }

    public Preference getById(Long id){
        return this.entityManager.find(Preference.class, id);
    }

    public List<Preference> getPreferencesByLocationId(Long locationId){
        return this.entityManager.createQuery("select p from Preference p where p.locationId = :locationId", Preference.class)
                .setParameter("locationId", locationId)
                .getResultList();
    }

    public List<Preference> getPreferencesByMinTempMaxTemp(double minTemp, double maxTemp){
        return this.entityManager.createQuery("select p from Preference p where p.minTemperature > :minTemp and p.maxTemperature < :maxTemp", Preference.class)
                .setParameter("minTemp", minTemp)
                .setParameter("maxTemp", maxTemp)
                .getResultList();
    }

    public void save(Preference pref) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(pref);
        this.entityManager.getTransaction().commit();
    }

    public void remove(Preference preference){
        this.entityManager.getTransaction().begin();
        this.entityManager.remove(preference);
        this.entityManager.getTransaction().commit();
    }

    public void update(Preference preference){
        this.entityManager.getTransaction().begin();
        this.entityManager.merge(preference);
        this.entityManager.getTransaction().commit();
    }
}
