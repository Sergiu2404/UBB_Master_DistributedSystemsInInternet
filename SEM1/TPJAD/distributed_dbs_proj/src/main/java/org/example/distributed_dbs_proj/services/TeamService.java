package org.example.distributed_dbs_proj.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.distributed_dbs_proj.models.Championship;
import org.example.distributed_dbs_proj.models.Team;

import java.util.List;

@Stateless
public class TeamService {
    @PersistenceContext(unitName = "oraclePU")
    private EntityManager entityManager;

    public Team create(String name, Long championshipId) {
        if (name == null || name.isBlank() || championshipId == null)
            throw new IllegalArgumentException("All fields (name, championshipId) are required");
        
        Championship ch = entityManager.find(Championship.class, championshipId);
        if (ch == null) throw new IllegalArgumentException("Championship not found: " + championshipId);
        
        Team t = new Team(name, ch);
        
        entityManager.persist(t);
        return t;
    }

    public Team find(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        return entityManager.find(Team.class, id);
    }

    public Team update(Long id, String name, Long championshipId) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Team existing = entityManager.find(Team.class, id);

        if (existing == null) throw new IllegalArgumentException("Team not found: " + id);
        if (name != null && !name.isBlank()) existing.setName(name);
        if (championshipId != null) {
            Championship ch = entityManager.find(Championship.class, championshipId);
            if (ch == null) throw new IllegalArgumentException("Championship not found: " + championshipId);
            existing.setChampionship(ch);
        }

        return entityManager.merge(existing);
    }

    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Team t = entityManager.find(Team.class, id);

        if (t != null) entityManager.remove(t);
    }

    public List<Team> all() {
        return entityManager.createQuery("SELECT t FROM Team t ORDER BY t.name", Team.class).getResultList();
    }

    public List<Team> byChampionship(Long championshipId) {
        return entityManager.createQuery("SELECT t FROM Team t WHERE t.championship.id = :cid ORDER BY t.name", Team.class)
                .setParameter("cid", championshipId)
                .getResultList();
    }
}
