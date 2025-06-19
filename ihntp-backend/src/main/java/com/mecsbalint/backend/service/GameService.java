package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final TagRepository tagRepository;
    private final ImageStorageService imageStorageService;

    @Autowired
    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, TagRepository tagRepository, ImageStorageService imageStorageService) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
        this.imageStorageService = imageStorageService;
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

    public Long addGame(GameToAdd gameToAdd, List<MultipartFile> screenshots, MultipartFile headerImg) {
        if (!checkForRequiredData(gameToAdd)) throw new MissingDataException(gameToAdd.toString(), "Game");

        Game game = createGameFromGameToAdd(gameToAdd);

        try {
            game = gameRepository.saveAndFlush(game);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                throw new ElementIsAlreadyInDatabaseException(game.toString(), "Game");
            } else {
                throw exception;
            }
        }

        game.setScreenshots(new HashSet<>());
        saveAndSetImages(game, screenshots, headerImg);

        return gameRepository.save(game).getId();
    }

    public void editGame(Long gameId, GameToEdit gameToEdit, List<MultipartFile> screenshots, MultipartFile headerImg) {
        if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

        Game gameOg = gameRepository.findGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", gameId.toString()));

        Game game = createGameFromGameToEdit(gameToEdit);
        game.setId(gameId);

        deleteUnnecessaryFiles(gameOg, game);

        game.setHeaderImg(game.getHeaderImg());
        game.setScreenshots(game.getScreenshots().stream()
                .filter(screenshot -> gameOg.getScreenshots().contains(screenshot))
                .collect(Collectors.toSet())
        );

        saveAndSetImages(game, screenshots, headerImg);

        gameRepository.save(game);

    }

    private void deleteUnnecessaryFiles(Game gameOg, Game gameNew) {
        List<String> imagesToDelete = new ArrayList<>();

        if (gameOg.getHeaderImg() != null && gameNew.getHeaderImg() == null) imagesToDelete.add(gameOg.getHeaderImg());

        List<String> screenshotsToDelete = gameOg.getScreenshots().stream()
                .filter(screenshot -> !gameNew.getScreenshots().contains(screenshot) && !screenshot.contains("http"))
                .toList();
        imagesToDelete.addAll(screenshotsToDelete);

        imageStorageService.deleteFiles(imagesToDelete);
    }

    private void saveAndSetImages(Game game, List<MultipartFile> screenshots, MultipartFile headerImg) {
        if (screenshots != null) {
            imageStorageService.validateImages(screenshots);
            Set<String> screenshotPaths = imageStorageService.saveImages(screenshots, game.getId() + "\\screenshots");
            game.getScreenshots().addAll(screenshotPaths);
        }

        if (headerImg != null) {
            imageStorageService.validateImages(List.of(headerImg));
            String headerImgPath = imageStorageService.saveImage(headerImg, game.getId() + "\\header_img");
            game.setHeaderImg(headerImgPath);
        }
    }

    private Game createGameFromGameToAdd(GameToAdd gameToAdd) {
        return createGame(gameToAdd.name(), gameToAdd.releaseDate(), gameToAdd.descriptionShort(), gameToAdd.descriptionLong(), gameToAdd.developerIds(), gameToAdd.publisherIds(), gameToAdd.tagIds());
    }

    private Game createGameFromGameToEdit(GameToEdit gameToEdit) {
        Game game = createGame(gameToEdit.name(), gameToEdit.releaseDate(), gameToEdit.descriptionShort(), gameToEdit.descriptionLong(), gameToEdit.developerIds(), gameToEdit.publisherIds(), gameToEdit.tagIds());
        game.setHeaderImg(gameToEdit.headerImg());
        game.setScreenshots(gameToEdit.screenshots());

        return game;
    }

    private Game createGame(String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, Set<Long> developerIds, Set<Long> publisherIds, Set<Long> tagIds) {
        Game game = new Game();
        game.setName(name);
        game.setReleaseDate(releaseDate);
        game.setDescriptionShort(descriptionShort);
        game.setDescriptionLong(descriptionLong);
        game.setDevelopers(new HashSet<>(developerRepository.findAllById(developerIds)));
        game.setPublishers(new HashSet<>(publisherRepository.findAllById(publisherIds)));
        game.setTags(new HashSet<>(tagRepository.findAllById(tagIds)));

        return game;
    }

    private boolean checkForRequiredData(GameToEdit game) {
        return checkForRequiredData(game.name(), game.releaseDate(), game.tagIds(), game.developerIds(), game.publisherIds());
    }

    private boolean checkForRequiredData(GameToAdd game) {
        return checkForRequiredData(game.name(), game.releaseDate(), game.tagIds(), game.developerIds(), game.publisherIds());
    }

    private boolean checkForRequiredData(String name, LocalDate releaseDate, Set<Long> tagIds, Set<Long> developerIds, Set<Long> publisherIds) {
        if (name.isEmpty()) return false;
        if (releaseDate == null) return false;
        if (tagIds.isEmpty()) return false;
        if (developerIds.isEmpty()) return false;
        if (publisherIds.isEmpty()) return false;

        return true;
    }
}
