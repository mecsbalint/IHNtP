package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameStatusDto;
import com.mecsbalint.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addGameToWishlist(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.addGameToWishlist(gameId, authUserEmail);
    }

    @PutMapping("/backlog/{gameId}")
    public void addGameToBacklog(@PathVariable long gameId) {
        String authUserEmail = getAuthenticatedUserEmail();

        userService.addGameToBacklog(gameId, authUserEmail);
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
