package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.UserGame;

public record UserGameForGameProfileDto(boolean isWishlist, boolean isBacklog) {
    public UserGameForGameProfileDto(UserGame userGame) {
        this(
          userGame.isWishlist(),
          userGame.isBacklog()
        );
    }
}
