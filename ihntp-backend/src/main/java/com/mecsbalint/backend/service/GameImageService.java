package com.mecsbalint.backend.service;

import org.springframework.stereotype.Service;

@Service
public class GameImageService {

    private final ImageStorageService imageStorageService;

    public GameImageService(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }
}
