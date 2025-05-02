package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToMany
    private List<Game> games;
}
