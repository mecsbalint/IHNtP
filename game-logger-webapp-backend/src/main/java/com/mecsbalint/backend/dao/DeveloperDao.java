package com.mecsbalint.backend.dao;

import com.mecsbalint.backend.model.Developer;

import java.util.List;

public class DeveloperDao implements TrackerAppDao<Developer>{

    @Override
    public void add(Developer element) {

    }

    @Override
    public Developer read(int id) {
        return null;
    }

    @Override
    public List<Developer> readAll() {
        return List.of();
    }

    @Override
    public void update(Developer element) {

    }

    @Override
    public void deleteById(int id) {

    }
}
