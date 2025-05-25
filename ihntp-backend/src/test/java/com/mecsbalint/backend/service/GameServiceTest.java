package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.*;

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
    public void getGameById_existingId_ReturnGameDto() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(getGame()));

        String expectedGameName = "Game One";
        String actualGameName = gameService.getGameForProfileById(1L).name();

        assertEquals(expectedGameName, actualGameName);
    }

    @Test
    public void getGameById_notExistingId_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenThrow(new GameNotFoundException("", ""));

        assertThrows(GameNotFoundException.class, () -> gameService.getGameForProfileById(1L));
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
}