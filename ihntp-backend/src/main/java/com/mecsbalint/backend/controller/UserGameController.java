package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameStatusDto;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/games")
public class UserGameController {

    private final UserService userService;

    @Autowired
    public UserGameController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/wishlist")
    public Set<GameForListDto> getUserWishlist(HttpServletRequest request) {
        UserEntity user = userService.getUserFromRequest(request);

        return user.getWishlist().stream()
                .map(GameForListDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/backlog")
    public Set<GameForListDto> getUserBacklog(HttpServletRequest request) {
        UserEntity user = userService.getUserFromRequest(request);

        return user.getBacklog().stream()
                .map(GameForListDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/status/{gameId}")
    public GameStatusDto getGameStatusById(@PathVariable long gameId, HttpServletRequest request) {
        return userService.getGameStatus(gameId, request);
    }

    @PutMapping("/wishlist/{gameId}")
    public ResponseEntity<Void> addGameToWishlist(@PathVariable long gameId, HttpServletRequest request) {
        userService.addGameToWishlist(gameId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/backlog/{gameId}")
    public ResponseEntity<Void> addGameToBacklog(@PathVariable long gameId, HttpServletRequest request) {
        userService.addGameToBacklog(gameId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/wishlist/{gameId}")
    public long removeGameFromWishlist(@PathVariable long gameId, HttpServletRequest request) {
        userService.removeGameFromWishlist(gameId, request);

        return gameId;
    }

    @DeleteMapping("/backlog/{gameId}")
    public long removeGameFromBacklog(@PathVariable long gameId, HttpServletRequest request) {
        userService.removeGameFromBacklog(gameId, request);

        return gameId;
    }
}
