package org.example.distributed_dbs_proj.services;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.distributed_dbs_proj.models.Player;
import org.example.distributed_dbs_proj.models.Team;

import java.util.List;

@Stateless
public class PlayerService {
    @PersistenceContext(unitName = "oraclePU")
    private EntityManager entityManager;

    public Player create(String name, Double value, Long teamId) {
        if (name == null || name.isBlank() || value == null || teamId == null)
            throw new IllegalArgumentException("All fields are required");
        
        Team team = entityManager.find(Team.class, teamId);
        if (team == null) throw new IllegalArgumentException("Team not found: " + teamId);
        
        Player p = new Player(name, value, team);
        entityManager.persist(p);
        return p;
    }

    public Player find(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        return entityManager.find(Player.class, id);
    }

    public Player update(Long id, String name, Double value, Long teamId) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Player existing = entityManager.find(Player.class, id);

        if (existing == null) throw new IllegalArgumentException("Player not found: " + id);
        if (name != null && !name.isBlank()) existing.setName(name);
        if (value != null) existing.setValue(value);
        if (teamId != null) {
            Team team = entityManager.find(Team.class, teamId);
            if (team == null) throw new IllegalArgumentException("Team not found: " + teamId);
            existing.setTeam(team);
        }

        return entityManager.merge(existing);
    }

    public void delete(Long id) {
        if (id == null) throw new IllegalArgumentException("Id is required");
        Player p = entityManager.find(Player.class, id);

        if (p != null) entityManager.remove(p);
    }

    public List<Player> all() {
        return entityManager.createQuery("SELECT p FROM Player p ORDER BY p.name", Player.class).getResultList();
    }

    public List<Player> byTeam(Long teamId) {
        return entityManager.createQuery("SELECT p FROM Player p WHERE p.team.id = :tid ORDER BY p.name", Player.class)
                .setParameter("tid", teamId)
                .getResultList();
    }
}