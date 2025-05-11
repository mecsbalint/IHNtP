package com.mecsbalint.backend.controller.dto;

import java.util.Set;

public record JwtResponseDto(String jwt, String name, Set<String> roles) {
}
