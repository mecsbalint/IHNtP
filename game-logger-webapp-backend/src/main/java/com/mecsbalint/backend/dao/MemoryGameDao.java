package com.mecsbalint.backend.dao;

import com.mecsbalint.backend.model.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryGameDao implements GameDao{
    private static int idCounter = 0;

    private final List<Game> games;

    public MemoryGameDao() {
        games = new ArrayList<>();
    }

    @Override
    public List<Game> getGames() {
        return games;
    }

    @Override
    public Game getGameById(int id) {
        return games.stream()
                .filter(game -> game.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public boolean addGame(Game game) {
        game.setId(++idCounter);
        return games.add(game);
    }

    @Override
    public boolean deleteGameById(int id) {
        return games.remove(getGameById(id));
    }
}
