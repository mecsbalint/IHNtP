package com.mecsbalint.backend.controller.dto.isthereanydealapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItadGameInfoGameDto(String id) {
}
