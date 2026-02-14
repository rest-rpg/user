package com.rest_rpg.user

import com.ms.user.model.CreateUserRequest
import com.ms.user.model.UserLite

class UserHelper {

    static boolean compare(CreateUserRequest request, UserLite userLite) {
        assert request.registerRequest.username == userLite.username
        assert request.registerRequest.email == userLite.email
        assert request.role == userLite.role
        return true
    }
}
