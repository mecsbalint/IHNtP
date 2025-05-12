package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Developer> developers;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Publisher> publishers;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(name, game.name) && Objects.equals(releaseDate, game.releaseDate) && Objects.equals(descriptionShort, game.descriptionShort) && Objects.equals(descriptionLong, game.descriptionLong) && Objects.equals(headerImg, game.headerImg) && Objects.equals(screenshots, game.screenshots) && Objects.equals(developers, game.developers) && Objects.equals(publishers, game.publishers) && Objects.equals(tags, game.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseDate, descriptionShort, descriptionLong, headerImg, screenshots, developers, publishers, tags);
    }
}
