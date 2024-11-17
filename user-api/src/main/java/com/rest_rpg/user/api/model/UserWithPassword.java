package com.rest_rpg.user.api.model;

public record UserWithPassword(long id, String username, String password, String email, boolean enabled, Role role) {
}
