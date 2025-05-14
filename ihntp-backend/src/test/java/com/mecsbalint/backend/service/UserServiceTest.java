package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameStatusDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInSetException;
import com.mecsbalint.backend.exception.ElementNotFoundInSetException;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoderMock;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    GameRepository gameRepositoryMock;

    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    public void setup() {
        userService = new UserService(
                passwordEncoderMock,
                userRepositoryMock,
                gameRepositoryMock
        );

        userEntity = new UserEntity();
        userEntity.setName("Authenticated User");
        userEntity.setBacklog(generateBacklog());
        userEntity.setWishlist(generateWishlist());
        when(userRepositoryMock.findByEmail(any())).thenReturn(Optional.of(userEntity));
    }

    @ParameterizedTest
    @MethodSource("provideGetGameStatusTestData")
    public void getGameStatus_ParameterizedTest(long gameId, GameStatusDto expectedGameStatusDto) {
        GameStatusDto actualGameStatusDto = userService.getGameStatus(gameId, null);

        assertEquals(expectedGameStatusDto, actualGameStatusDto);
    }

    @Test
    public void addGameToWishlist_gameNotExist_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> userService.addGameToWishlist(1L, null));
    }

    @Test
    public void addGameToWishlist_gameExistAndNotInWishlist_userRepositorySaveCalled() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(new Game()));

        userService.addGameToWishlist(0L, null);

        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void addGameToWishlist_gameExistAndInWishlist_throwElementIsAlreadyInSetException() {
        Game gameToAdd = new Game();
        gameToAdd.setId(1L);
        userEntity.getWishlist().add(gameToAdd);
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(gameToAdd));

        assertThrows(ElementIsAlreadyInSetException.class, () -> userService.addGameToWishlist(0L, null));
    }

    @Test
    public void addGameToBacklog_gameNotExist_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> userService.addGameToBacklog(1L, null));
    }

    @Test
    public void addGameToBacklog_gameExistAndNotInBacklog_userRepositorySaveCalled() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(new Game()));

        userService.addGameToBacklog(0L, null);

        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void addGameToBacklog_gameExistAndInBacklog_throwElementIsAlreadyInSetException() {
        Game gameToAdd = new Game();
        gameToAdd.setId(1L);
        userEntity.getBacklog().add(gameToAdd);
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(gameToAdd));

        assertThrows(ElementIsAlreadyInSetException.class, () -> userService.addGameToBacklog(0L, null));
    }

    @Test
    public void removeGameFromBacklog_gameNotExist_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> userService.removeGameFromBacklog(1L, null));
    }

    @Test
    public void removeGameFromBacklog_gameExistAndInBacklog_userRepositorySaveCalled() {
        Game gameToRemove = new Game();
        gameToRemove.setId(1L);
        userEntity.getBacklog().add(gameToRemove);
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(gameToRemove));

        userService.removeGameFromBacklog(0L, null);

        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void removeGameFromBacklog_gameExistAndNotInBacklog_throwElementNotFoundInSetException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(new Game()));

        assertThrows(ElementNotFoundInSetException.class, () -> userService.removeGameFromBacklog(0L, null));
    }

    @Test
    public void removeGameFromWishlist_gameNotExist_throwGameNotFoundException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> userService.removeGameFromWishlist(1L, null));
    }

    @Test
    public void removeGameFromWishlist_gameExistAndInWishlist_userRepositorySaveCalled() {
        Game gameToRemove = new Game();
        gameToRemove.setId(1L);
        userEntity.getWishlist().add(gameToRemove);
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(gameToRemove));

        userService.removeGameFromWishlist(0L, null);

        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void removeGameFromWishlist_gameExistAndNotInWishlist_throwElementNotFoundInSetException() {
        when(gameRepositoryMock.getGameById(any())).thenReturn(Optional.of(new Game()));

        assertThrows(ElementNotFoundInSetException.class, () -> userService.removeGameFromWishlist(0L, null));
    }

    private static Stream<Arguments> provideGetGameStatusTestData() {
        return Stream.of(
          Arguments.of(1L, new GameStatusDto(true, true)),
          Arguments.of(3L, new GameStatusDto(true, false)),
          Arguments.of(6L, new GameStatusDto(false, true)),
          Arguments.of(10L, new GameStatusDto(false, false))
        );
    }

    private Set<Game> generateWishlist() {
        Game game1 = new Game();
        game1.setId(1L);
        Game game2 = new Game();
        game2.setId(2L);
        Game game3 = new Game();
        game3.setId(3L);
        Game game4 = new Game();
        game4.setId(4L);
        return new HashSet<>(Arrays.asList(game1, game2, game3, game4));
    }

    private Set<Game> generateBacklog() {
        Game game1 = new Game();
        game1.setId(1L);
        Game game2 = new Game();
        game2.setId(2L);
        Game game3 = new Game();
        game3.setId(5L);
        Game game4 = new Game();
        game4.setId(6L);
        return new HashSet<>(Arrays.asList(game1, game2, game3, game4));
    }
}