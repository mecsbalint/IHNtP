package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameAddDto;
import com.mecsbalint.backend.controller.dto.GameDto;
import com.mecsbalint.backend.dao.GameDao;
import com.mecsbalint.backend.model.Game;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GameService {
    private final GameDao gameDao;


    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public List<GameDto> getGames() {
        return gameDao.getGames().stream()
                .map(GameDto::new)
                .toList();
    }

    public GameDto getGameById(int id) {
        Game game = gameDao.getGameById(id);
        if (game == null) throw new NoSuchElementException();
        return new GameDto(game);
    }

    public boolean addGame(GameAddDto gameAddDto) {
        return gameDao.addGame(new Game(gameAddDto));
    }
}
