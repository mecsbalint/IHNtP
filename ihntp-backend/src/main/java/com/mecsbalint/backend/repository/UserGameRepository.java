package com.mecsbalint.backend.repository;

import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.UserEntity;
import com.mecsbalint.backend.model.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> getUserGameByGame(Game game);

    Set<UserGame> getUserGamesByUserAndBacklog(UserEntity user, boolean backlog);

    Set<UserGame> getUserGamesByUserAndWishlist(UserEntity user, boolean wishlist);

}
