package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameStatusDto;
import com.mecsbalint.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user/games")
public class UserGameController {

    private final UserService userService;

    @Autowired
    public UserGameController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/wishlist")
    public Set<GameForListDto> getUserWishlist() {
        String authUserEmail = getAuthenticatedUserEmail();

        return userService.getUserWishlist(authUserEmail);
    }

    @GetMapping("/backlog")
    public Set<GameForListDto> getUserBacklog() {
        String authUserEmail = getAuthenticatedUserEmail();

        return userService.getUserBacklog(authUserEmail);
    }

    @GetMapping("/status/{gameId}")
    public GameStatusDto getGameStatusById(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        return userService.getGameStatus(gameId, authUserEmail);
    }

    @PutMapping("/wishlist/{gameId}")
    public ResponseEntity<Void> addGameToWishlist(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.addGameToWishlist(gameId, authUserEmail);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/backlog/{gameId}")
    public ResponseEntity<Void> addGameToBacklog(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.addGameToBacklog(gameId, authUserEmail);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/wishlist/{gameId}")
    public long removeGameFromWishlist(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.removeGameFromWishlist(gameId, authUserEmail);

        return gameId;
    }

    @DeleteMapping("/backlog/{gameId}")
    public long removeGameFromBacklog(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.removeGameFromBacklog(gameId, authUserEmail);

        return gameId;
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
