package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.controller.dto.game.GamePricesDto;

import java.util.Optional;

public interface GamePriceService {
    Optional<GamePricesDto> getGamePrices(String gameName, String userCountry);
}
