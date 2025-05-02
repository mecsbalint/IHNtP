package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

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

    @ManyToMany
    private List<Developer> developers;

    @ManyToMany
    private List<Publisher> publishers;

    @ManyToMany
    private List<Tag> tags;
}
