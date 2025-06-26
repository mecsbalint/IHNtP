package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.*;
import com.mecsbalint.backend.controller.dto.isthereanydealapi.*;
import com.mecsbalint.backend.exception.*;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import com.mecsbalint.backend.utility.Fetcher;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
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
    private final UserService userService;
    private final Fetcher fetcher;
    private final String itadApiKey;

    @Autowired
    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, TagRepository tagRepository, ImageStorageService imageStorageService, UserService userService, Fetcher fetcher, @Value("${mecsbalint.app.itadApiKey}")String itadApiKey) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
        this.imageStorageService = imageStorageService;
        this.userService = userService;
        this.fetcher = fetcher;
        this.itadApiKey = itadApiKey;
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

        GamePricesDto gamePrices = getGamePriceDataFromItad(gameEntity, userCountryCode).orElse(null);

        return new GameForGameProfileDto(gameEntity, gamePrices);
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

        setHeaderImg(game, null, headerImg);

        game.setScreenshots(new HashSet<>());
        game.getScreenshots().addAll(saveScreenshots(game, screenshots));
        game.getScreenshots().addAll(downloadScreenshots(game, Set.of()));

        return gameRepository.save(game).getId();
    }

    public void editGame(Long gameId, GameToEdit gameToEdit, List<MultipartFile> screenshots, MultipartFile headerImg) {
        if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

        Game gameOg = gameRepository.findGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", gameId.toString()));

        Game gameNew = createGameFromGameToEdit(gameToEdit);
        gameNew.setId(gameId);

        deleteUnnecessaryFiles(gameOg, gameNew);

        setHeaderImg(gameNew, gameOg.getHeaderImg(), headerImg);

        setScreenshots(gameNew, gameOg, screenshots);

        gameRepository.save(gameNew);
    }

    private void deleteUnnecessaryFiles(Game gameOg, Game gameNew) {
        List<String> imagesToDelete = new ArrayList<>();

        if (gameOg.getHeaderImg() != null && gameNew.getHeaderImg() != gameOg.getHeaderImg()) imagesToDelete.add(gameOg.getHeaderImg());

        List<String> screenshotsToDelete = gameOg.getScreenshots().stream()
                .filter(screenshot -> !gameNew.getScreenshots().contains(screenshot) && !screenshot.contains("http"))
                .toList();
        imagesToDelete.addAll(screenshotsToDelete);

        imageStorageService.deleteFiles(imagesToDelete);
    }

    private void setHeaderImg(Game gameNew, String headerImgOg, MultipartFile headerImgFile) {
        String headerImgNew = gameNew.getHeaderImg();
        if (headerImgFile != null) {
            if (!imageStorageService.validateImages(List.of(headerImgFile))) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");
            String headerImgPath = imageStorageService.saveImage(headerImgFile, gameNew.getId() + "\\header_img");
            gameNew.setHeaderImg(headerImgPath);
        } else if(headerImgNew != headerImgOg) {
            if (headerImgNew == null) {
                gameNew.setHeaderImg(null);
            } else {
                gameNew.setHeaderImg(downloadHeaderImg(headerImgNew, gameNew.getId()));
            }
        }
    }

    private void setScreenshots(Game gameNew, Game gameOg, List<MultipartFile> screenshots) {
        Set<String> screenshotsRemaining = gameNew.getScreenshots().stream()
                .filter(screenshot -> gameOg.getScreenshots().contains(screenshot))
                .collect(Collectors.toSet());

        Set<String> screenshotsFromFiles = saveScreenshots(gameNew, screenshots);

        Set<String> screenshotsFromLinks = downloadScreenshots(gameNew, gameOg.getScreenshots());

        gameNew.setScreenshots(screenshotsRemaining);
        gameNew.getScreenshots().addAll(screenshotsFromFiles);
        gameNew.getScreenshots().addAll(screenshotsFromLinks);
    }

    private String downloadHeaderImg(String link, long gameId) {
        MultipartFile file = fetcher.fetch(link, MultipartFile.class);

        if (!imageStorageService.validateImages(List.of(file))) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");

        return imageStorageService.saveImage(file, gameId + "\\header_img");
    }

    private Set<String> downloadScreenshots(Game gameNew, Set<String> screenshotsOg) {
        Set<String> screenshotLinks = gameNew.getScreenshots().stream()
                .filter(screenshot -> !screenshotsOg.contains(screenshot))
                .collect(Collectors.toSet());

        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (String link : screenshotLinks) {
            multipartFiles.add(fetcher.fetch(link, MultipartFile.class));
        }
        if (!imageStorageService.validateImages(multipartFiles)) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");

        return imageStorageService.saveImages(multipartFiles, gameNew.getId() + "\\screenshots");
    }

    private Set<String> saveScreenshots(Game game, List<MultipartFile> screenshots) {
        if (screenshots != null) {
            if (!imageStorageService.validateImages(screenshots)) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");
            return imageStorageService.saveImages(screenshots, game.getId() + "\\screenshots");
        }
        return Set.of();
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

    private Optional<GamePricesDto> getGamePriceDataFromItad(Game game, String userCountry) {
        String gameInfoFetchUrl = String.format("https://api.isthereanydeal.com/games/lookup/v1?key=%s&title=%s", itadApiKey, game.getName());
        ItadGameInfoDto gameInfo = fetcher.fetch(gameInfoFetchUrl, ItadGameInfoDto.class);

        if (gameInfo.game() == null) return Optional.empty();

        if (userCountry == null) userCountry = "US";
        String itadGameId = gameInfo.game().id();
        String priceInfoFetchUrl = String.format("https://api.isthereanydeal.com/games/prices/v3?key=%s&deals=false&country=%s", itadApiKey, userCountry);
        ItadGamePriceInfoDto[] gamePrices = fetcher.fetch(priceInfoFetchUrl, ItadGamePriceInfoDto[].class, HttpMethod.POST, "application/json", new String[]{itadGameId});

        if (gamePrices.length == 0) return Optional.empty();

        ItadGamePriceDealDto currentBestPrice = gamePrices[0].deals().stream()
                .min(Comparator.comparingDouble(a -> a.price().amount())).get();
        ItadPriceDto historyLowPrice = gamePrices[0].historyLow().all();

        GamePriceDto currentPrice = new GamePriceDto(currentBestPrice.price().currency(), currentBestPrice.price().amount(), currentBestPrice.url());
        String priceHistoryUrl = String.format("https://isthereanydeal.com/game/id:%s/history/", itadGameId);
        GamePriceDto historyLow = new GamePriceDto(historyLowPrice.currency(), historyLowPrice.amount(), priceHistoryUrl);
        return Optional.of(new GamePricesDto(currentPrice, historyLow));
    }
}
