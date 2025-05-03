package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;

public record GameDto(long id, String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, String headerImg, List<String> screenshots, List<DeveloperForGameDto> developers, List<PublisherForGameDto> publishers, List<TagForGameDto> tags) {
    public GameDto(Game gameEntity) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getDescriptionShort(),
                gameEntity.getDescriptionLong(),
                gameEntity.getHeaderImg(),
                gameEntity.getScreenshots(),
                gameEntity.getDevelopers().stream().map(DeveloperForGameDto::new).toList(),
                gameEntity.getPublishers().stream().map(PublisherForGameDto::new).toList(),
                gameEntity.getTags().stream().map(TagForGameDto::new).toList()
        );
    }
}
