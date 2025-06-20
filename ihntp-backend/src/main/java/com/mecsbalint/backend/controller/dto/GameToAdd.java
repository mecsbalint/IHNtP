package com.mecsbalint.backend.controller.dto;

import java.time.LocalDate;
import java.util.Set;

public record GameToAdd(String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, Set<Long> developerIds, Set<Long> publisherIds, Set<Long> tagIds) {
}
