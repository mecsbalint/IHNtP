package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.developer.DeveloperToAdd;
import com.mecsbalint.backend.controller.dto.developer.DeveloperIdNameDto;
import com.mecsbalint.backend.service.developer.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {
    private final DeveloperService developerService;

    @Autowired
    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping
    public List<DeveloperIdNameDto> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Long> addDevelopers(@RequestBody List<DeveloperToAdd> developersToAdd) {
        return developerService.addDevelopers(developersToAdd);
    }
}
