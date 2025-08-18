package org.example.distributed_dbs_proj.models;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private double value;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public Player() {}
    public Player(String name, double value, Team team) {
        this.name = name;
        this.value = value;
        this.team = team;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}
