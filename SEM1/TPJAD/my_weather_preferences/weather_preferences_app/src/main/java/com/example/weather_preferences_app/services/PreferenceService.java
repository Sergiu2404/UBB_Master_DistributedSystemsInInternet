package com.example.weather_preferences_app.services;

import com.example.weather_preferences_app.entities.Preference;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PreferenceService {
    @PersistenceContext(unitName = "PU_Oracle")
    private EntityManager entityManager;

    public List<Preference> getAll(){
        return this.entityManager.createQuery("select p from Preference p", Preference.class)
                .getResultList();
    }

    public Preference getById(Long id){
        return this.entityManager.find(Preference.class, id);
    }

    public List<Preference> getPreferencesByLocationId(Long locationId) {
        return entityManager.createQuery("SELECT p FROM Preference p WHERE p.locationId = :locationId", Preference.class)
                .setParameter("locationId", locationId)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removePreferencesByLocationId(Long locationId) {
        entityManager.createQuery("DELETE FROM Preference p WHERE p.locationId = :locationId")
                .setParameter("locationId", locationId)
                .executeUpdate();
        entityManager.flush();
    }

    public List<Preference> getPreferencesByMinTempMaxTemp(double minTemp, double maxTemp){
        return this.entityManager.createQuery("select p from Preference p where p.minTemperature > :minTemp and p.maxTemperature < :maxTemp", Preference.class)
                .setParameter("minTemp", minTemp)
                .setParameter("maxTemp", maxTemp)
                .getResultList();
    }

    public void save(Preference pref) {
        this.entityManager.persist(pref);
    }

    public void remove(Preference preference){
        this.entityManager.remove(preference);
    }

    public void removePreferenceById(Long preferenceId) {
        this.entityManager.createQuery("DELETE FROM Preference p WHERE p.id = :preferenceId")
                .setParameter("preferenceId", preferenceId)
                .executeUpdate();
        this.entityManager.flush(); // force immediate execution to ensure is made in this transaction
    }

    public void update(Preference preference){
        this.entityManager.merge(preference);
    }
}
