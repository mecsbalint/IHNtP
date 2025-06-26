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
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
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

    public Long addGame(GameToAdd gameToAdd, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {
        if (!checkForRequiredData(gameToAdd)) throw new MissingDataException(gameToAdd.toString(), "Game");

        Game game = createGameFromGameToAdd(gameToAdd);
        String headerImgToAdd = game.getHeaderImg();
        game.setHeaderImg(null);
        Set<String> screenshotsToAdd = game.getScreenshots();
        game.setScreenshots(new HashSet<>());

        try {
            game = gameRepository.saveAndFlush(game);
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
        Set<String> savedScreenshotPaths = saveScreenshots(screenshotFiles, game.getId() + "\\screenshots");
        game.getScreenshots().addAll(savedScreenshotPaths);

        return gameRepository.save(game).getId();
    }

    public void editGame(Long gameId, GameToEdit gameToEdit, List<MultipartFile> screenshotFile, MultipartFile headerImgFile) {
        if (!checkForRequiredData(gameToEdit)) throw new MissingDataException(gameToEdit.toString(), "Game");

        Game gameOg = gameRepository.findGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", gameId.toString()));
        Game game = createGameFromGameToEdit(gameToEdit);
        game.setId(gameId);
    }

    private String handleHeaderImg(Long gameId, String headerImgNew, String headerImgOg, MultipartFile headerImgFile) {
        if (headerImgFile != null) {
            return saveHeaderImg(headerImgFile, gameId);
        } else if (headerImgNew == null) {
            return null;
        } else if (headerImgNew.equals(headerImgOg)) {
            return headerImgOg;
        } else {
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
        if (imagePath == null) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");
        return imagePath;
    }

    private String saveHeaderImg(MultipartFile headerImg, Long gameId) {
        if (!imageStorageService.validateMultipartFileImages(headerImg)) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");
        return imageStorageService.saveImage(headerImg, gameId + "\\header_img");
    }

    private Set<String> saveScreenshots(List<MultipartFile> screenshotFiles, String savePath) {
        if (screenshotFiles != null) {
            if (!imageStorageService.validateMultipartFileImages(screenshotFiles)) throw new InvalidFileException("JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO");
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
