package org.example.distributed_dbs_proj.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.distributed_dbs_proj.models.Championship;
import org.example.distributed_dbs_proj.models.Team;

import java.util.List;

@Stateless
public class ChampionshipService {
    @PersistenceContext(unitName = "pgPU")
    private EntityManager entityManager;

    public Championship create(String name, String countryName) {
        if (name == null || name.isBlank() || countryName == null || countryName.isBlank())
            throw new IllegalArgumentException("All fields (name, countryName) are required");
        
        Championship c = new Championship(name, countryName);
        entityManager.persist(c);
        return c;
    }

    public Championship find(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        return entityManager.find(Championship.class, id);
    }

    public Championship update(Long id, String name, String countryName) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Championship existing = entityManager.find(Championship.class, id);

        if (existing == null) throw new IllegalArgumentException("Championship not found: " + id);
        if (name != null && !name.isBlank()) existing.setName(name);
        if (countryName != null && !countryName.isBlank()) existing.setCountryName(countryName);

        return entityManager.merge(existing);
    }

    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Championship c = entityManager.find(Championship.class, id);

        if (c != null) entityManager.remove(c);
    }

    public List<Championship> all() {
        return entityManager.createQuery("SELECT c FROM Championship c ORDER BY c.name", Championship.class).getResultList();
    }
}
