package com.mecsbalint.backend.controller.dto.user;

import java.util.Set;

public record JwtResponseDto(String jwt, String name, Set<String> roles) {
}
