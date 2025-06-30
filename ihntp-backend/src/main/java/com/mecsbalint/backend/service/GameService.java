package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.*;
import com.mecsbalint.backend.exception.*;
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
    private final LocalImageStorageService imageStorageService;
    private final UserService userService;
    private final GamePriceService gamePriceService;

    @Autowired
    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, TagRepository tagRepository, LocalImageStorageService imageStorageService, UserService userService, GamePriceService gamePriceService) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
        this.imageStorageService = imageStorageService;
        this.userService = userService;
        this.gamePriceService = gamePriceService;
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
        Long gameId = null;
        try {
            if (!checkForRequiredData(gameToAdd)) throw new MissingDataException(gameToAdd.toString(), "Game");

            Game game = createGameFromGameToAdd(gameToAdd);
            String headerImgToAdd = game.getHeaderImg();
            game.setHeaderImg(null);
            Set<String> screenshotsToAdd = game.getScreenshots();
            game.setScreenshots(new HashSet<>());

            try {
                game = gameRepository.saveAndFlush(game);
                gameId = game.getId();
            } catch (DataIntegrityViolationException exception) {
                if (exception.getCause() instanceof ConstraintViolationException) {
                    throw new ElementIsAlreadyInDatabaseException(game.toString(), "Game");
                } else {
                    throw exception;
                }
            }

            game.setHeaderImg(handleHeaderImg(game.getId(), headerImgToAdd, null, headerImgFile));

            Set<String> downloadedScreenshotPaths = downloadAndSaveImages(screenshotsToAdd, game.getId() + "\\screenshots");
            game.getScreenshots().addAll(downloadedScreenshotPaths);

            Set<String> savedScreenshotPaths = getSavedScreenshots(screenshotFiles, gameId + "\\screenshots");
            game.getScreenshots().addAll(savedScreenshotPaths);

            return gameRepository.save(game).getId();
        } catch (Exception e) {
            if (gameId != null)imageStorageService.deleteFolder(gameId.toString());
            throw e;
        }
    }

    @Transactional
    public void editGame(Long gameId, GameToEdit gameToEdit, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {
        Set<String> screenshotsToKeep = null;

        try {
            if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

            Game gameOg = gameRepository.findGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", gameId.toString()));
            Game game = createGameFromGameToEdit(gameToEdit);
            game.setId(gameId);

            game.setHeaderImg(handleHeaderImg(gameId, game.getHeaderImg(), gameOg.getHeaderImg(), headerImgFile));

            Set<String> screenshotsToDelete = gameOg.getScreenshots().stream()
                    .filter(screenshot -> !game.getScreenshots().contains(screenshot) && !screenshot.contains("http"))
                    .collect(Collectors.toSet());
            screenshotsToKeep = game.getScreenshots().stream()
                    .filter(screenshot -> gameOg.getScreenshots().contains(screenshot))
                    .collect(Collectors.toSet());
            Set<String> screenshotsToDownload = game.getScreenshots().stream()
                    .filter(screenshot -> !gameOg.getScreenshots().contains(screenshot))
                    .collect(Collectors.toSet());

            game.setScreenshots(new HashSet<>());
            Set<String> gameScreenshots = game.getScreenshots();

            imageStorageService.deleteFiles(screenshotsToDelete);
            gameScreenshots.addAll(getDownloadedScreenshotsPaths(screenshotsToDownload, gameId));
            gameScreenshots.addAll(screenshotsToKeep);
            gameScreenshots.addAll(getSavedScreenshots(screenshotFiles, gameId + "\\screenshots"));

            gameRepository.save(game);
        } catch (Exception e) {
            if (screenshotsToKeep != null) imageStorageService.deleteFolderContent(gameId.toString(), screenshotsToKeep);
            throw e;
        }
    }

    private Set<String> getDownloadedScreenshotsPaths(Set<String> links, Long gameId) {
        return imageStorageService.downloadAndSaveImages(links, gameId + "\\screenshots").stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private String handleHeaderImg(Long gameId, String headerImgNew, String headerImgOg, MultipartFile headerImgFile) {
        if (headerImgFile != null) {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return saveHeaderImg(headerImgFile, gameId);
        } else if (headerImgNew == null) {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return null;
        } else if (headerImgNew.equals(headerImgOg)) {
            return headerImgOg;
        } else {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return downloadAndSaveImage(headerImgNew, gameId + "\\header_img");
        }
    }

    private Set<String> downloadAndSaveImages(Set<String> links, String savePath) {
        Set<String> paths = new HashSet<>();

        for (String link : links) {
            paths.add(downloadAndSaveImage(link, savePath));
        }

        return paths;
    }

    private String downloadAndSaveImage(String link, String savePath) {
        String imagePath = imageStorageService.downloadAndSaveImage(link, savePath);
        if (imagePath == null) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
        return imagePath;
    }

    private String saveHeaderImg(MultipartFile headerImg, Long gameId) {
        if (!imageStorageService.validateMultipartFileImages(headerImg)) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
        return imageStorageService.saveImage(headerImg, gameId + "\\header_img");
    }

    private Set<String> getSavedScreenshots(List<MultipartFile> screenshotFiles, String savePath) {
        if (screenshotFiles != null) {
            if (!imageStorageService.validateMultipartFileImages(screenshotFiles)) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
            return imageStorageService.saveImages(screenshotFiles, savePath);
        }
        return new HashSet<>();
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
