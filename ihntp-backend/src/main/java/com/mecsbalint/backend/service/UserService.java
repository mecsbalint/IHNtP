package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameStatusDto;
import com.mecsbalint.backend.controller.dto.UserRegistrationDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInSetException;
import com.mecsbalint.backend.exception.ElementNotFoundInSetException;
import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.exception.UserNotFoundException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.UserRole;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, GameRepository gameRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public boolean saveUser(UserRegistrationDto userRegistration) {
        if (userRepository.findByEmail(userRegistration.email()).isPresent()) {
            return false;
        }

        if (!List.of(Locale.getISOCountries()).contains(userRegistration.countryCode())) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setName(userRegistration.name());
        user.setEmail(userRegistration.email());
        user.setPassword(passwordEncoder.encode(userRegistration.password()));
        user.setRoles(Set.of(UserRole.ROLE_USER));

        userRepository.save(user);

        return true;
    }

    public UserEntity getUserByEmail(String userEmail) {
        return userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
    }

    public Set<GameForListDto> getUserWishlist(String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);

        return user.getWishlist().stream()
                .map(GameForListDto::new)
                .collect(Collectors.toSet());
    }

    public Set<GameForListDto> getUserBacklog(String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);

        return user.getBacklog().stream()
                .map(GameForListDto::new)
                .collect(Collectors.toSet());
    }

    public GameStatusDto getGameStatus(long gameId, String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);

        Set<Long> backlogGameIds = user.getBacklog().stream().map(Game::getId).collect(Collectors.toSet());
        Set<Long> wishlistGameIds = user.getWishlist().stream().map(Game::getId).collect(Collectors.toSet());

        boolean wishlistStatus = wishlistGameIds.contains(gameId);
        boolean backlogStatus = backlogGameIds.contains(gameId);

        return new GameStatusDto(wishlistStatus, backlogStatus);
    }

    public void addGameToWishlist(long gameId, String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);
        Game game = getGameById(gameId);

        if (user.getWishlist().add(game)) {
            userRepository.save(user);
        } else {
            throw new ElementIsAlreadyInSetException(game.toString());
        }
    }

    public void addGameToBacklog(long gameId, String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);
        Game game = getGameById(gameId);

        if (user.getBacklog().add(game)) {
            userRepository.save(user);
        } else {
            throw new ElementIsAlreadyInSetException(game.toString());
        }
    }

    public void removeGameFromWishlist(long gameId, String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);
        Game game = getGameById(gameId);

        if (user.getWishlist().remove(game)) {
            userRepository.save(user);
        } else {
            throw new ElementNotFoundInSetException(game.toString());
        }
    }

    public void removeGameFromBacklog(long gameId, String authUserEmail) {
        UserEntity user = getUserByEmail(authUserEmail);
        Game game = getGameById(gameId);

        if (user.getBacklog().remove(game)) {
            userRepository.save(user);
        } else {
            throw new ElementNotFoundInSetException(game.toString());
        }
    }

    private Game getGameById(long gameId) {
        return gameRepository.getGameById(gameId).orElseThrow(() -> new GameNotFoundException("id", String.valueOf(gameId)));
    }
}