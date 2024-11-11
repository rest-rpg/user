package com.rest_rpg.user.feign;

import com.rest_rpg.user.api.model.UserAuth;
import com.rest_rpg.user.api.model.UserLite;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface UserInternalApi {

    @RequestMapping(method = RequestMethod.GET, value = "/user/name/{username}")
    UserAuth getUserByUsername(@PathVariable @NotBlank String username);

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    UserLite getUserById(@PathVariable long userId);
}
