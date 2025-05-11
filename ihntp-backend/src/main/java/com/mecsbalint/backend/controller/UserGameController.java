package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.GameForListDto;
import com.mecsbalint.backend.controller.dto.UserGameForGameProfileDto;
import com.mecsbalint.backend.exception.IllegalRequestParameterException;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.security.jwt.AuthTokenFilter;
import com.mecsbalint.backend.security.jwt.JwtUtils;
import com.mecsbalint.backend.service.UserGameService;
import com.mecsbalint.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user/games")
public class UserGameController {

    private final UserGameService userGameService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public UserGameController(UserGameService userGameService, UserService userService, JwtUtils jwtUtils, AuthTokenFilter authTokenFilter) {
        this.userGameService = userGameService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authTokenFilter = authTokenFilter;
    }

    @GetMapping("/game-by-id/{id}")
    public UserGameForGameProfileDto getUserGameByGameId(@PathVariable long id) {
        return userGameService.getUserGameByGameId(id);
    }

    @GetMapping
    public Set<GameForListDto> getGamesByUserLists(@RequestParam String filteredBy, HttpServletRequest request) {
        if (!Set.of("wishlist", "backlog").contains(filteredBy)) throw new IllegalRequestParameterException(filteredBy);

        String jwt = authTokenFilter.parseJwt(request);
        String email = jwtUtils.getUserNameFromJwtToken(jwt);
        UserEntity user = userService.getUserByEmail(email);

        if (filteredBy.equals("wishlist")) {
            return userGameService.getWishlistGamesByUser(user);
        } else if (filteredBy.equals("backlog")) {
            return userGameService.getBacklogGamesByUser(user);
        }

        return Set.of();
    }
}
