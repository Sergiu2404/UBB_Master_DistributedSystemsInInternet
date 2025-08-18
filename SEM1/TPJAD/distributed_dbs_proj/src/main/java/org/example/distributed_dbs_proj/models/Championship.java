package org.example.distributed_dbs_proj.models;

import jakarta.persistence.*;

@Entity
@Table(name = "championships")
public class Championship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "country", nullable = false, length = 100)
    private String countryName;

    public Championship(){}
    public Championship(String name, String countryName){
        this.name = name;
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
