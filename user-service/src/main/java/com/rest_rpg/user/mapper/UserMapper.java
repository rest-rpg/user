package com.rest_rpg.user.mapper;

import com.rest_rpg.user.api.model.UserWithPassword;
import com.rest_rpg.user.api.model.UserLite;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserLite toUserLite(@NotNull User user);

    UserWithPassword toUserWithPassword(@NotNull User user);
}
