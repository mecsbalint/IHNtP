package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.JwtResponseDto;
import com.mecsbalint.backend.controller.dto.UserEmailPasswordDto;
import com.mecsbalint.backend.controller.dto.UserRegistrationDto;
import com.mecsbalint.backend.exception.UserHasAlreadyExistException;
import com.mecsbalint.backend.security.jwt.JwtUtils;
import com.mecsbalint.backend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("api/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRegistrationDto userRegistration) {
        if (!userService.saveUser(userRegistration)) throw new UserHasAlreadyExistException(userRegistration.email());
    }

    @PostMapping("api/login")
    public JwtResponseDto authenticateUser(@RequestBody UserEmailPasswordDto userLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String userName = userService.getUserByEmail(userLogin.email()).getName();

        return new JwtResponseDto(jwt, userName, roles);
    }
}
