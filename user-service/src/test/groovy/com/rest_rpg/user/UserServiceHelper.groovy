package com.rest_rpg.user

import com.rest_rpg.user.api.model.Role
import com.rest_rpg.user.model.User
import com.rest_rpg.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import java.time.Instant

@Service
class UserServiceHelper {

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    def clean() {
        userRepository.deleteAll()
    }

    User createUser(Map customArgs = [:]) {
        Map args = [
                username        : "User" + Instant.now().toEpochMilli(),
                email           : "user" + Instant.now().toEpochMilli() + "@gmail.com",
                password        : "12345678",
                verificationCode: UUID.randomUUID().toString(),
                role            : Role.USER,
                enabled         : true
        ]
        args << customArgs

        def user = User.builder()
                .username(args.username)
                .email(args.email)
                .password(passwordEncoder.encode(args.password))
                .verificationCode(args.verificationCode)
                .role(args.role)
                .enabled(args.enabled)
                .build()
        userRepository.save(user)
    }

    User getUserByUsername(@NotBlank String username) {
        userRepository.getByUsername(username)
    }
}
