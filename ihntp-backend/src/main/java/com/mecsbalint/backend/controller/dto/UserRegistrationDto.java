package com.mecsbalint.backend.controller.dto;

public record UserRegistrationDto(String name, String email, String password, String countryCode) {
}
