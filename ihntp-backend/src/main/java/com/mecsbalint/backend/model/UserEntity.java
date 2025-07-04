package com.mecsbalint.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.Set;

@Entity
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
    private long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String countryCode;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Game> wishlist;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Game> backlog;
}
