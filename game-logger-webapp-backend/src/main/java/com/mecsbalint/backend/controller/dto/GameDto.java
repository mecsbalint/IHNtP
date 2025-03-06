package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;

public record GameDto(int id, String name, String developer, String publisher) {
    public GameDto(Game game) {
        this(game.getId(), game.getName(), game.getDeveloper(), game.getPublisher());
    }
}
