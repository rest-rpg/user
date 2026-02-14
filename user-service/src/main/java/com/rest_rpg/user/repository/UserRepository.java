package com.rest_rpg.user.repository;

import com.rest_rpg.user.exception.UserNotFoundException;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {

  Optional<User> findByUsernameAndDeletedFalse(@NotBlank String username);

  Optional<User> findByEmailAndDeletedFalse(@NotBlank String email);

  Optional<User> findByVerificationCodeAndDeletedFalse(@NotBlank String verificationCode);

  Optional<User> findByIdAndDeletedFalse(@NotNull UUID id);

  default @NotNull User getByUsername(@NotBlank String username) {
    return findByUsernameAndDeletedFalse(username).orElseThrow(UserNotFoundException::new);
  }

  default @NotNull User getByVerificationCode(@NotBlank String verificationCode) {
    return findByVerificationCodeAndDeletedFalse(verificationCode)
        .orElseThrow(UserNotFoundException::new);
  }

  default @NotNull User getByIdAndDeletedFalse(UUID userId) {
    return findByIdAndDeletedFalse(userId).orElseThrow(UserNotFoundException::new);
  }
}
