package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameForEditGameDto;
import com.mecsbalint.backend.controller.dto.GameForGameProfileDto;
import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameToAdd;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, TagRepository tagRepository) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public List<GameForListDto> getAllGamesSummary() {
        List<Game> gameEntities = gameRepository.findAll();
        return gameEntities.stream()
                .map(GameForListDto::new)
                .toList();
    }

    @Transactional
    public GameForGameProfileDto getGameForProfileById(long id) {
        Game gameEntity = gameRepository.getGameById(id).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(id)));
        return new GameForGameProfileDto(gameEntity);
    }

    @Transactional
    public GameForEditGameDto getGameForEditGameById(long id) {
        Game gameEntity = gameRepository.getGameById(id).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(id)));
        return new GameForEditGameDto(gameEntity);
    }

    public Long addGame(GameToAdd gameToAdd) {
        List<Developer> developers = developerRepository.findAllById(gameToAdd.developerIds());
        List<Publisher> publishers = publisherRepository.findAllById(gameToAdd.publisherIds());
        List<Tag> tags = tagRepository.findAllById(gameToAdd.tagIds());

        Game game = createGameFromGameToAdd(gameToAdd, developers, publishers, tags);

        try {
            return gameRepository.save(game).getId();
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                throw new ElementIsAlreadyInDatabaseException(game.toString(), "Game");
            } else {
                throw exception;
            }
        }
    }

    private Game createGameFromGameToAdd(GameToAdd gameToAdd, List<Developer> developers, List<Publisher> publishers, List<Tag> tags) {
        Game game = new Game();
        game.setName(gameToAdd.name());
        game.setReleaseDate(gameToAdd.releaseDate());
        game.setDescriptionShort(gameToAdd.descriptionShort());
        game.setDescriptionLong(gameToAdd.descriptionLong());
        game.setHeaderImg(gameToAdd.headerImg());
        game.setScreenshots(gameToAdd.screenshots());
        game.setDevelopers(new HashSet<>(developers));
        game.setPublishers(new HashSet<>(publishers));
        game.setTags(new HashSet<>(tags));

        return game;
    }
}
