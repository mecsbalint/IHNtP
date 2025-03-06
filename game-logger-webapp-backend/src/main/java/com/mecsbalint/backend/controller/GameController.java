package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameAddDto;
import com.mecsbalint.backend.controller.dto.GameDto;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/all")
    public List<GameDto> getGames() {
        return gameService.getGames();
    }

    @GetMapping("/{id}")
    public GameDto getGameById(@PathVariable int id) {
        return gameService.getGameById(id);
    }

    @PostMapping("")
    public boolean addGame(@RequestBody GameAddDto gameAddDto) {
        return gameService.addGame(gameAddDto);
    }
}
