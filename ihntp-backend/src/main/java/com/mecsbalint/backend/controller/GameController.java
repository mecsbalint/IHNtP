package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameDto;
import com.mecsbalint.backend.controller.dto.GameSummaryDto;
import com.mecsbalint.backend.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/all")
    public List<GameSummaryDto> getAllGamesSummary() {
        return gameService.getAllGamesSummary();
    }

    @GetMapping("/{id}")
    public GameDto getGameById(@PathVariable long id) {
        return gameService.getGameById(id);
    }
}
