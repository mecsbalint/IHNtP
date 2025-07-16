package com.mecsbalint.backend.controller.dto.game;

import com.mecsbalint.backend.controller.dto.tag.TagIdNameDto;
import com.mecsbalint.backend.model.Game;

import java.time.LocalDate;
import java.util.List;

public record GameForListDto(long id, String name, LocalDate releaseDate, String headerImg, List<TagIdNameDto> tags) {
    public GameForListDto(Game gameEntity) {
        this(
                gameEntity.getId(),
                gameEntity.getName(),
                gameEntity.getReleaseDate(),
                gameEntity.getHeaderImg(),
                gameEntity.getTags().stream().map(TagIdNameDto::new).toList()
        );
    }
}
