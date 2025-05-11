package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record GameForGameProfileDto(long id, String name, LocalDate releaseDate, String descriptionLong, Set<String> screenshots, List<DeveloperForGameDto> developers, List<PublisherForGameDto> publishers, List<TagForGameDto> tags) {
    public GameForGameProfileDto(Game gameEntity) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getDescriptionLong(),
                gameEntity.getScreenshots(),
                gameEntity.getDevelopers().stream().map(DeveloperForGameDto::new).toList(),
                gameEntity.getPublishers().stream().map(PublisherForGameDto::new).toList(),
                gameEntity.getTags().stream().map(TagForGameDto::new).toList()
        );
    }
}
