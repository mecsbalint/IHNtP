package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.DeveloperToAdd;
import com.mecsbalint.backend.controller.dto.DeveloperIdNameDto;
import com.mecsbalint.backend.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/all")
    public List<DeveloperIdNameDto> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    @PostMapping("/add")
    public List<Long> addDevelopers(@RequestBody List<DeveloperToAdd> developersToAdd) {
        return developerService.addDevelopers(developersToAdd);
    }
}
