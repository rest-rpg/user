package com.rest_rpg.user.repository;

import com.ms.user.model.UserFilter;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

  Page<User> findUsers(@NotNull UserFilter filter, @NotNull Pageable pageable);
}
