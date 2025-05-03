package com.mecsbalint.backend.controller.dto;

import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;

public record GameSummaryDto(long id, String name, LocalDate releaseDate, String headerImg, List<TagForGameDto> tags) {
    public GameSummaryDto(Game gameEntity) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getHeaderImg(),
                gameEntity.getTags().stream().map(TagForGameDto::new).toList()
        );
    }
}
