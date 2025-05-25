package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForEditGameDto;
import com.mecsbalint.backend.controller.dto.GameForGameProfileDto;
import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.GameToAdd;
import com.mecsbalint.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
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

    @PostMapping("/add")
    public Long addGame(@RequestBody GameToAdd gameToAdd) {
        return gameService.addGame(gameToAdd);
    }
}
