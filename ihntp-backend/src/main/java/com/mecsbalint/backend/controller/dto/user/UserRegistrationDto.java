package com.mecsbalint.backend.controller.dto.user;

public record UserRegistrationDto(String name, String email, String password, String countryCode) {
}
