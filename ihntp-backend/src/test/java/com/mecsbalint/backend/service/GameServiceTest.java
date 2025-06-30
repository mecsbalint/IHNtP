package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.*;
import com.mecsbalint.backend.exception.*;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;

    @Mock
    private DeveloperRepository developerRepositoryMock;

    @Mock
    private PublisherRepository publisherRepositoryMock;

    @Mock
    private TagRepository tagRepositoryMock;

    @Mock
    private LocalImageStorageService imageStorageServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private GamePriceService gamePriceServiceMock;

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService(gameRepositoryMock, developerRepositoryMock, publisherRepositoryMock, tagRepositoryMock, imageStorageServiceMock, userServiceMock, gamePriceServiceMock);
    }

    @Test
    public void getAllGamesSummary_noGames_returnEmptyList() {
        when(gameRepositoryMock.findAll()).thenReturn(List.of());

        int expectedListLength = 0;
        int actualListLength = gameService.getAllGamesSummary().size();

        assertEquals(expectedListLength, actualListLength);
    }

    @Test
    public void getAllGamesSummary_threeGames_returnListOfTheGames() {
        when(gameRepositoryMock.findAll()).thenReturn(getListOfGames());

        List<GameForListDto> expectedGames = getListOfGames().stream().map(GameForListDto::new).toList();
        List<GameForListDto> actualGames = gameService.getAllGamesSummary();

        assertEquals(expectedGames.get(0).name(), actualGames.get(0).name());
        assertEquals(expectedGames.get(1).name(), actualGames.get(1).name());
        assertEquals(expectedGames.get(2).name(), actualGames.get(2).name());
    }

    @Test
    public void getGameForProfileById_existingId_ReturnGameDto() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));
        when(userServiceMock.getUserByEmail(any())).thenReturn(new UserEntity());

        String expectedGameName = "Game One";
        String actualGameName = gameService.getGameForProfileById(1L, null).name();

        assertEquals(expectedGameName, actualGameName);
    }

    @Test
    public void getGameForProfileById_cannotFetchTheGameItadId_gamePricesIsNull() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));
        when(userServiceMock.getUserByEmail(any())).thenReturn(new UserEntity());

        GamePricesDto actualGamePrices = gameService.getGameForProfileById(1L, null).gamePrices();

        assertNull(actualGamePrices);
    }

    @Test
    public void getGameForProfileById_cannotFetchPrices_gamePricesIsNull() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));
        when(userServiceMock.getUserByEmail(any())).thenReturn(new UserEntity());

        GamePricesDto actualGamePrices = gameService.getGameForProfileById(1L, null).gamePrices();

        assertNull(actualGamePrices);
    }

    @Test
    public void getGameForProfileById_successfulFetches_gamePricesIsGamePricesDto() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));
        when(userServiceMock.getUserByEmail(any())).thenReturn(new UserEntity());

        GamePriceDto currentGamePriceDto = new GamePriceDto("EUR", 10, "url");
        GamePricesDto gamePricesDto = new GamePricesDto(currentGamePriceDto, null);
        when(gamePriceServiceMock.getGamePrices(any(), any())).thenReturn(Optional.of(gamePricesDto));

        double expectedCurrentPrice = 10;
        double actualCurrentPrice = gameService.getGameForProfileById(1L, null).gamePrices().current().amount();

        assertEquals(expectedCurrentPrice, actualCurrentPrice);
    }

    @Test
    public void getGameForProfileById_notExistingId_throwGameNotFoundException() {
        when(userServiceMock.getUserByEmail(any())).thenThrow(new UserNotFoundException(""));
        when(gameRepositoryMock.getGameById(any())).thenThrow(new GameNotFoundException("", ""));

        assertThrows(GameNotFoundException.class, () -> gameService.getGameForProfileById(1L, null));
    }

    @Test
    public void getGameForEditGameById_existingId_ReturnGameDto() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));

        String expectedGameName = "Game One";
        String actualGameName = gameService.getGameForEditGameById(1L).name();

        assertEquals(expectedGameName, actualGameName);
    }

    @Test
    public void getGameForEditGameById_notExistingId_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenThrow(new GameNotFoundException("", ""));

        assertThrows(GameNotFoundException.class, () -> gameService.getGameForEditGameById(1L));
    }

    @Test
    public void addGame_happyCaseWithNoFiles_callSaveAndFlushAndSaveMethods() {

    }

    @Test
    public void addGame_gameNotHaveRequiredData_throwMissingDataException() {
        Game incompleteGame = getGame();
        incompleteGame.setName("");
        incompleteGame.setReleaseDate(null);

        assertThrows(MissingDataException.class, () -> gameService.addGame(getGameToAdd(incompleteGame), null ,null));
    }

    @Test
    public void addGame_gameIsAlreadyExist_throwsElementIsAlreadyInDatabaseException() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Constraint violation", null, "SomeConstraint");
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("", constraintViolationException);
        when(gameRepositoryMock.saveAndFlush(any())).thenThrow(dataIntegrityViolationException);

        assertThrows(ElementIsAlreadyInDatabaseException.class, () -> gameService.addGame(getGameToAdd(getGame()), null, null));
    }

    @Test
    public void editGame_happyCaseWithNoFiles_callSave() {
        when(gameRepositoryMock.findGameById(any())).thenReturn(Optional.of(getGame()));

        gameService.editGame(1L, getGameToEdit(getGame()), null, null);

        verify(gameRepositoryMock).save(any());
    }

    @Test
    public void editGame_happyCaseWithFilesBecomeUnnecessary_callDeleteFiles() {
        when(gameRepositoryMock.findGameById(any())).thenReturn(Optional.of(getGame()));
        Game newGame = getGame();
        newGame.setHeaderImg(null);

        gameService.editGame(1L, getGameToEdit(newGame), null, null);

        verify(imageStorageServiceMock).deleteFiles(any());
    }

    @Test
    public void editGame_gameNotHaveRequiredData_throwMissingDataException() {
        Game incompleteGame = getGame();
        incompleteGame.setName("");
        incompleteGame.setReleaseDate(null);

        assertThrows(MissingDataException.class, () -> gameService.editGame(1L, getGameToEdit(incompleteGame), null, null));
    }

    @Test
    public void editGame_gameNotExist_throwsGameNotFoundException() {
        when(gameRepositoryMock.findGameById(any())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.editGame(1L, getGameToEdit(getGame()), null, null));
    }

    private Game getGame() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Game One");
        game.setReleaseDate(LocalDate.of(2020, 1, 15));
        game.setDescriptionShort("");
        game.setDescriptionLong("");
        game.setHeaderImg("https://example.com/img1.jpg");
        game.setScreenshots(Set.of());
        game.setDevelopers(Set.of());
        game.setPublishers(Set.of());
        game.setTags(new HashSet<>());

        return game;
    }

    private GameToAdd getGameToAdd(Game game) {
        return new GameToAdd(
                game.getName(),
                game.getReleaseDate(),
                game.getDescriptionShort(),
                game.getDescriptionLong(),
                game.getHeaderImg(),
                game.getScreenshots(),
                Set.of(1L),
                Set.of(1L),
                Set.of(1L)
        );
    }

    private GameToEdit getGameToEdit(Game game) {
        return new GameToEdit(
                game.getName(),
                game.getReleaseDate(),
                game.getDescriptionShort(),
                game.getDescriptionLong(),
                game.getHeaderImg(),
                game.getScreenshots(),
                Set.of(1L),
                Set.of(1L),
                Set.of(1L)
        );
    }

    private List<Game> getListOfGames() {
        Game game1 = new Game();
        game1.setId(1L);
        game1.setName("Game One");
        game1.setReleaseDate(LocalDate.of(2020, 1, 15));
        game1.setHeaderImg("https://example.com/img1.jpg");
        game1.setTags(new HashSet<>());

        Game game2 = new Game();
        game2.setId(2L);
        game2.setName("Game Two");
        game2.setReleaseDate(LocalDate.of(2021, 6, 10));
        game2.setHeaderImg("https://example.com/img2.jpg");
        game2.setTags(new HashSet<>());

        Game game3 = new Game();
        game3.setId(3L);
        game3.setName("Game Three");
        game3.setReleaseDate(LocalDate.of(2022, 11, 5));
        game3.setHeaderImg("https://example.com/img3.jpg");
        game3.setTags(new HashSet<>());

        return List.of(game1, game2, game3);
    }

    private MultipartFile getMultipartFileMock() {
        return new MockMultipartFile("file", "image.png", "image/png", new byte[]{1, 2, 3});
    }
}