package com.mecsbalint.backend.controller.dto.game;

import java.time.LocalDate;
import java.util.Set;

public record GameToAdd(String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, String headerImg, Set<String> screenshots, Set<Long> developerIds, Set<Long> publisherIds, Set<Long> tagIds) {
}
