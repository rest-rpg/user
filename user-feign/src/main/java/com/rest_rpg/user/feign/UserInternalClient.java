package com.rest_rpg.user.feign;

import com.rest_rpg.user.api.model.UserLite;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@FeignClient(value = "user", url = "${feign.user.url}")
public interface UserInternalClient extends UserInternalApi {

    default String getUsernameFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    default UserLite getUserFromContext() {
        String username = getUsernameFromContext();

        return getUserLiteByUsername(username);
    }

    default boolean doesCharacterBelongToUser(long userId) {
        UserLite user = getUserById(userId);

        return Objects.equals(user.username(), getUsernameFromContext());
    }
}
