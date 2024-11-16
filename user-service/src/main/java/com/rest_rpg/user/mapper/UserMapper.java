package com.rest_rpg.user.mapper;

import com.rest_rpg.user.api.model.UserAuth;
import com.rest_rpg.user.api.model.UserLite;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserLite toUserLite(@NotNull User user);

    @Mapping(target = "authorities", ignore = true)
    UserAuth toUserAuth(@NotNull User user);
}
