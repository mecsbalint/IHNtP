package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDate releaseDate;

    private String descriptionShort;

    private String descriptionLong;

    private String headerImg;

    @ElementCollection
    private Set<String> screenshots;

    @ManyToMany
    private Set<Developer> developers;

    @ManyToMany
    private Set<Publisher> publishers;

    @ManyToMany
    private Set<Tag> tags;
}
