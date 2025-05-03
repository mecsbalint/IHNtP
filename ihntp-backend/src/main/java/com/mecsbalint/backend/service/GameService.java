package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameDto;
import com.mecsbalint.backend.controller.dto.GameSummaryDto;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameSummaryDto> getAllGamesSummary() {
        List<Game> gameEntities = gameRepository.findAll();
        return gameEntities.stream()
                .map(GameSummaryDto::new)
                .toList();
    }

    public GameDto getGameById(long id) {
        Game gameEntity = gameRepository.getGameById(id).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(id)));
        return new GameDto(gameEntity);
    }
}
