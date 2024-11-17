package com.rest_rpg.user.repository;

import com.rest_rpg.user.exception.UserNotFoundException;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    Optional<User> findByVerificationCode(@NotBlank String verificationCode);

    default @NotNull User getByUsername(@NotBlank String username) {
        return findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    default @NotNull User getByVerificationCode(@NotBlank String verificationCode) {
        return findByVerificationCode(verificationCode).orElseThrow(UserNotFoundException::new);
    }

    default @NotNull User getById(long userId) {
        return findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
