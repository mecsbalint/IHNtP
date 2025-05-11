package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
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
    private List<String> screenshots;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Developer> developers;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Publisher> publishers;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserGame> userGames;
}
