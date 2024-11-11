package com.rest_rpg.user.mapper;

import com.rest_rpg.user.api.model.UserLite;
import com.rest_rpg.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserLite toUserLite(User user);
}
