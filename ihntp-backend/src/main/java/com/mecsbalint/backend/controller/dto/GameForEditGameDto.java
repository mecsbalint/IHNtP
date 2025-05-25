package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record GameForEditGameDto(long id, String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, String headerImg, Set<String> screenshots, List<DeveloperIdNameDto> developers, List<PublisherIdNameDto> publishers, List<TagIdNameDto> tags) {
    public GameForEditGameDto(Game gameEntity) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getDescriptionShort(),
                gameEntity.getDescriptionLong(),
                gameEntity.getHeaderImg(),
                gameEntity.getScreenshots(),
                gameEntity.getDevelopers().stream().map(DeveloperIdNameDto::new).toList(),
                gameEntity.getPublishers().stream().map(PublisherIdNameDto::new).toList(),
                gameEntity.getTags().stream().map(TagIdNameDto::new).toList()
        );
    }
}
