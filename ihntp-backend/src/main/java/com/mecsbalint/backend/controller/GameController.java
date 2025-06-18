package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForEditGameDto;
import com.mecsbalint.backend.controller.dto.GameForGameProfileDto;
import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameToAdd;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService, GameRepository gameRepository) {
        this.gameService = gameService;
    }

    @GetMapping("/all")
    public List<GameForListDto> getAllGamesSummary() {
        return gameService.getAllGamesSummary();
    }

    @GetMapping("/profile/{id}")
    public GameForGameProfileDto getGameForProfileById(@PathVariable long id) {
        return gameService.getGameForProfileById(id);
    }

    @GetMapping("/edit/{id}")
    public GameForEditGameDto getGameForEditGameById(@PathVariable long id) {
        return gameService.getGameForEditGameById(id);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> editGame(@PathVariable Long id, @RequestPart("game") GameToAdd gameToEdit, @RequestPart(value = "screenshots", required = false) List<MultipartFile> screenshots, @RequestPart(value = "headerImg", required = false) MultipartFile headerImg) {
        gameService.editGame(id, gameToEdit, screenshots, headerImg);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/add")
    public Long addGame(@RequestPart("game") GameToAdd gameToAdd, @RequestPart(value = "screenshots", required = false) List<MultipartFile> screenshots, @RequestPart(value = "headerImg", required = false) MultipartFile headerImg) {
        return gameService.addGame(gameToAdd, screenshots, headerImg);
    }
}
