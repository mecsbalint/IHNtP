package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.UserGameForGameProfileDto;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.exception.UserGameNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.model.UserGame;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.UserGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGameService {

    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;

    @Autowired
    public UserGameService(UserGameRepository userGameRepository, GameRepository gameRepository) {
        this.userGameRepository = userGameRepository;
        this.gameRepository = gameRepository;
    }

    public UserGameForGameProfileDto getUserGameByGameId(long gameId) {
        Game gameEntity = gameRepository.getGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(gameId)));
        UserGame userGame = userGameRepository.getUserGameByGame(gameEntity).orElseThrow(() -> new UserGameNotFoundException("Game Id", String.valueOf(gameId)));

        return new UserGameForGameProfileDto(userGame);
    }

    public Set<GameForListDto> getWishlistGamesByUser(UserEntity user) {
        return userGameRepository.getUserGamesByUserAndWishlist(user, true).stream()
                .map(userGame -> new GameForListDto(userGame.getGame()))
                .collect(Collectors.toSet());
    }

    public Set<GameForListDto> getBacklogGamesByUser(UserEntity user) {
        return userGameRepository.getUserGamesByUserAndBacklog(user, true).stream()
                .map(userGame -> new GameForListDto(userGame.getGame()))
                .collect(Collectors.toSet());
    }
}
