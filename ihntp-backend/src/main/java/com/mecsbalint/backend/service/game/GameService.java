package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.controller.dto.*;
import com.mecsbalint.backend.exception.*;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import com.mecsbalint.backend.service.user.UserService;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final GamePriceService gamePriceService;
    private final GameImageService gameImageService;

    @Autowired
    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, TagRepository tagRepository, UserService userService, GamePriceService gamePriceService, GameImageService gameImageService) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.gamePriceService = gamePriceService;
        this.gameImageService = gameImageService;
    }

    @Transactional
    public List<GameForListDto> getAllGamesSummary() {
        List<Game> gameEntities = gameRepository.findAll();
        return gameEntities.stream()
                .map(GameForListDto::new)
                .toList();
    }

    @Transactional
    public GameForGameProfileDto getGameForProfileById(long id, String userEmail) {
        String userCountryCode;

        try {
            userCountryCode = userService.getUserByEmail(userEmail).getCountryCode();
        } catch (UserNotFoundException e) {
            userCountryCode = null;
        }

        Game gameEntity = gameRepository.getGameById(id).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(id)));

        GamePricesDto gamePrices = gamePriceService.getGamePrices(gameEntity.getName(), userCountryCode).orElse(null);

        return new GameForGameProfileDto(gameEntity, gamePrices);
    }

    @Transactional
    public GameForEditGameDto getGameForEditGameById(long id) {
        Game gameEntity = gameRepository.getGameById(id).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(id)));
        return new GameForEditGameDto(gameEntity);
    }

    @Transactional
    public Long addGame(GameToAdd gameToAdd, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {
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

        gameImageService.handleImagesForAddGame(game, screenshotFiles, headerImgFile);

        return gameRepository.save(game).getId();
    }

    @Transactional
    public void editGame(Long gameId, GameToEdit gameToEdit, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {

        if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

        Game gameOg = gameRepository.findGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", gameId.toString()));
        Game gameNew = createGameFromGameToEdit(gameToEdit);
        gameNew.setId(gameId);

        gameImageService.handleImagesForEditGame(gameId, gameNew, gameOg, screenshotFiles, headerImgFile);

        gameRepository.save(gameNew);
    }


    private Game createGameFromGameToAdd(GameToAdd gameToAdd) {
        Game game = createGame(gameToAdd.name(), gameToAdd.releaseDate(), gameToAdd.descriptionShort(), gameToAdd.descriptionLong(), gameToAdd.developerIds(), gameToAdd.publisherIds(), gameToAdd.tagIds());
        game.setHeaderImg(gameToAdd.headerImg());
        game.setScreenshots(gameToAdd.screenshots());

        return game;
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
