package com.mecsbalint.backend.controller.dto;

import java.time.LocalDate;
import java.util.Set;

public record GameToEdit(String name, LocalDate releaseDate, String descriptionShort, String descriptionLong, String headerImg, Set<String> screenshots, Set<Long> developerIds, Set<Long> publisherIds, Set<Long> tagIds) {
}
