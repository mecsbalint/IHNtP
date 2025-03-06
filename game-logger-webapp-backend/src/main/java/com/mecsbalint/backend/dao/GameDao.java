package com.mecsbalint.backend.dao;

import com.mecsbalint.backend.model.Game;

import java.util.List;

public interface GameDao {

    List<Game> getGames();

    Game getGameById(int id);

    boolean addGame(Game game);

    boolean deleteGameById(int id);
}
