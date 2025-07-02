package com.mecsbalint.backend.utility;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProvider {
    public UUID getRandomUUID() {
        return UUID.randomUUID();
    }
}
