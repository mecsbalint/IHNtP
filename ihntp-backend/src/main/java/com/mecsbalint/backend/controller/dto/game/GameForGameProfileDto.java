package com.mecsbalint.backend.controller.dto.game;

import com.mecsbalint.backend.controller.dto.publisher.PublisherIdNameDto;
import com.mecsbalint.backend.controller.dto.tag.TagIdNameDto;
import com.mecsbalint.backend.controller.dto.developer.DeveloperIdNameDto;
import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record GameForGameProfileDto(long id, String name, LocalDate releaseDate, String descriptionLong, Set<String> screenshots, List<DeveloperIdNameDto> developers, List<PublisherIdNameDto> publishers, List<TagIdNameDto> tags, GamePricesDto gamePrices) {
    public GameForGameProfileDto(Game gameEntity, GamePricesDto gamePricesDto) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getDescriptionLong(),
                gameEntity.getScreenshots(),
                gameEntity.getDevelopers().stream().map(DeveloperIdNameDto::new).toList(),
                gameEntity.getPublishers().stream().map(PublisherIdNameDto::new).toList(),
                gameEntity.getTags().stream().map(TagIdNameDto::new).toList(),
                gamePricesDto
        );
    }
}
