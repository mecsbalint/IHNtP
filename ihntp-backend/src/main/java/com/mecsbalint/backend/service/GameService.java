package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameForEditGameDto;
import com.mecsbalint.backend.controller.dto.GameForGameProfileDto;
import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameToAdd;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.exception.MissingDataException;
import com.mecsbalint.backend.model.Game;
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
        if (!checkForRequiredData(gameToAdd)) throw new MissingDataException(gameToAdd.toString(), "Game");

        Game game = createGameFromGameToAdd(gameToAdd);

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

    public void editGame(GameToAdd gameToEdit, Long gameId) {
        if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

        if (gameRepository.findGameById(gameId).isPresent()) {
            Game game = createGameFromGameToAdd(gameToEdit);
            game.setId(gameId);

            gameRepository.save(game);
        } else {
            throw new GameNotFoundException("id", gameId.toString());
        }
    }

    private Game createGameFromGameToAdd(GameToAdd gameToAdd) {
        Game game = new Game();
        game.setName(gameToAdd.name());
        game.setReleaseDate(gameToAdd.releaseDate());
        game.setDescriptionShort(gameToAdd.descriptionShort());
        game.setDescriptionLong(gameToAdd.descriptionLong());
        game.setHeaderImg(gameToAdd.headerImg());
        game.setScreenshots(gameToAdd.screenshots());
        game.setDevelopers(new HashSet<>(developerRepository.findAllById(gameToAdd.developerIds())));
        game.setPublishers(new HashSet<>(publisherRepository.findAllById(gameToAdd.publisherIds())));
        game.setTags(new HashSet<>(tagRepository.findAllById(gameToAdd.tagIds())));

        return game;
    }

    private boolean checkForRequiredData(GameToAdd game) {
        if (game.name().isEmpty()) return false;
        if (game.releaseDate() == null) return false;
        if (game.tagIds().isEmpty()) return false;
        if (game.developerIds().isEmpty()) return false;
        if (game.publisherIds().isEmpty()) return false;

        return true;
    }
}
