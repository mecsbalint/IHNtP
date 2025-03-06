package com.mecsbalint.backend.dao;

import com.mecsbalint.backend.model.Game;

import java.util.List;

public class GameDao implements TrackerAppDao<Game> {

    @Override
    public void add(Game element) {

    }

    @Override
    public Game read(int id) {
        return null;
    }

    @Override
    public List<Game> readAll() {
        return List.of();
    }

    @Override
    public void update(Game element) {

    }

    @Override
    public void deleteById(int id) {

    }
}
