package com.mecsbalint.backend.model;

import com.mecsbalint.backend.controller.dto.GameAddDto;

public class Game{
    private int id;
    private String name;
    private String developer;
    private String publisher;

    public Game(int id, String name, String developer, String publisher) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
    }

    public Game(GameAddDto gameAddDto) {
        this.name = gameAddDto.name();
        this.developer = gameAddDto.developer();
        this.publisher = gameAddDto.publisher();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setId(int id) {
        this.id = id;
    }
}
