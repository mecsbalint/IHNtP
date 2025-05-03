package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameDto;
import com.mecsbalint.backend.controller.dto.GameSummaryDto;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService(gameRepositoryMock);
    }

    @AfterEach
    public void resetMocks() {
        reset(gameRepositoryMock);
    }

    @Test
    public void getAllGamesSummary_noGames_returnEmptyList() {
        when(gameRepositoryMock.findAll()).thenReturn(List.of());

        int expectedListLength = 0;
        int actualListLength = gameService.getAllGamesSummary().size();

        assertEquals(expectedListLength, actualListLength);
    }

    @Test
    public void getAllGamesSummary_threeGames_returnListOfThree() {
        when(gameRepositoryMock.findAll()).thenReturn(getListOfGames());

        int expectedListLength = 3;
        int actualListLength = gameService.getAllGamesSummary().size();

        assertEquals(expectedListLength, actualListLength);
    }

    @Test
    public void getAllGamesSummary_threeGames_returnGameSummaryDtoList() {
        when(gameRepositoryMock.findAll()).thenReturn(getListOfGames());

        GameSummaryDto actualGame1 = gameService.getAllGamesSummary().get(0);

        assertEquals(GameSummaryDto.class, actualGame1.getClass());
    }

    @Test
    public void getGameById_existingId_ReturnGameDto() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));

        GameDto actualGame = gameService.getGameById(1L);

        assertEquals(GameDto.class, actualGame.getClass());
    }

    @Test
    public void getGameById_notExistingId_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenThrow(new GameNotFoundException("", ""));

        assertThrows(GameNotFoundException.class, () -> gameService.getGameById(1L));
    }

    private Game getGame() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Game One");
        game.setReleaseDate(LocalDate.of(2020, 1, 15));
        game.setDescriptionShort("");
        game.setDescriptionLong("");
        game.setHeaderImg("https://example.com/img1.jpg");
        game.setScreenshots(List.of());
        game.setDevelopers(List.of());
        game.setPublishers(List.of());
        game.setTags(Collections.emptyList());

        return game;
    }

    private List<Game> getListOfGames() {
        Game game1 = new Game();
        game1.setId(1L);
        game1.setName("Game One");
        game1.setReleaseDate(LocalDate.of(2020, 1, 15));
        game1.setHeaderImg("https://example.com/img1.jpg");
        game1.setTags(Collections.emptyList());

        Game game2 = new Game();
        game2.setId(2L);
        game2.setName("Game Two");
        game2.setReleaseDate(LocalDate.of(2021, 6, 10));
        game2.setHeaderImg("https://example.com/img2.jpg");
        game2.setTags(Collections.emptyList());

        Game game3 = new Game();
        game3.setId(3L);
        game3.setName("Game Three");
        game3.setReleaseDate(LocalDate.of(2022, 11, 5));
        game3.setHeaderImg("https://example.com/img3.jpg");
        game3.setTags(Collections.emptyList());

        return List.of(game1, game2, game3);
    }
}