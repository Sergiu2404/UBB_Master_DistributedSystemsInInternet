package org.example.distributed_dbs_proj.models;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "championship_id", nullable = false)
    private Championship championship;

    public Team() {}
    public Team(String name, Championship championship) {
        this.name = name;
        this.championship = championship;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Championship getChampionship() { return championship; }
    public void setChampionship(Championship championship) { this.championship = championship; }

}
