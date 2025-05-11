package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserGame {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private long id;

    private boolean wishlist;

    private boolean backlog;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private Game game;
}
