package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.game.*;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService, GameRepository gameRepository) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameForListDto> getAllGamesSummary() {
        return gameService.getAllGamesSummary();
    }

    @GetMapping("/{id}")
    public GameForGameProfileDto getGameForProfileById(@PathVariable long id) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            return gameService.getGameForProfileById(id, currentUserEmail);
    }

    @GetMapping("/{id}/edit")
    public GameForEditGameDto getGameForEditGameById(@PathVariable long id) {
        return gameService.getGameForEditGameById(id);
    }

    @PutMapping("/{id}")
    public void editGame(@PathVariable Long id, @RequestPart("game") GameToEdit gameToEdit, @RequestPart(value = "screenshots", required = false) List<MultipartFile> screenshots, @RequestPart(value = "headerImg", required = false) MultipartFile headerImg) {
        gameService.editGame(id, gameToEdit, screenshots, headerImg);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addGame(@RequestPart("game") GameToAdd gameToAdd, @RequestPart(value = "screenshots", required = false) List<MultipartFile> screenshots, @RequestPart(value = "headerImg", required = false) MultipartFile headerImg) {
        return gameService.addGame(gameToAdd, screenshots, headerImg);
    }
}
