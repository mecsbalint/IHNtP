package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.UserRegistrationDto;
import com.mecsbalint.backend.exception.UserNotFoundException;
import com.mecsbalint.backend.model.UserRole;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean saveUser(UserRegistrationDto userRegistration) {
        if (userRepository.findByEmail(userRegistration.email()).isPresent()) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setName(userRegistration.name());
        user.setEmail(userRegistration.email());
        user.setPassword(passwordEncoder.encode(userRegistration.password()));
        user.setRoles(Set.of(UserRole.ROLE_USER));

        userRepository.save(user);

        return true;
    }

    public String getUserName(String userEmail) {
        return userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(String.format("There is no User with this e-amil: %s", userEmail)))
                .getName();
    }
}