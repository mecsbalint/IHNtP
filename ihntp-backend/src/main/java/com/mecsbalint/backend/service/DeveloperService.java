package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.DeveloperIdNameDto;
import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DeveloperService {
    private final DeveloperRepository developerRepository;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<DeveloperIdNameDto> getAllDevelopers() {
        List<Developer> developerEntities = developerRepository.findAll();

        return developerEntities.stream()
                .map(DeveloperIdNameDto::new)
                .sorted(Comparator.comparing(DeveloperIdNameDto::name))
                .toList();
    }
}
