package com.rest_rpg.user.api.model;

public record UserLite(long id, String username, String email, Role role) {
}
