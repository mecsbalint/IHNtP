package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "developers")
    private Set<Game> games;
}
