package com.rest_rpg.user.service;

import com.rest_rpg.mailer_api.model.SendVerificationEmailEvent;
import com.rest_rpg.user.api.model.Role;
import com.rest_rpg.user.exception.AccountEmailExistsException;
import com.rest_rpg.user.exception.AccountUsernameExistsException;
import com.rest_rpg.user.exception.UserAlreadyVerifiedException;
import com.rest_rpg.user.model.User;
import com.rest_rpg.user.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, SendVerificationEmailEvent> kafkaTemplate;

    @Value("${api-gateway.url}")
    private String apiGatewayUrl;

    public void register(@NotNull @Valid RegisterRequest request,
                         @NotNull Role role) {
        assertAccountNotExists(request.getUsername(), request.getEmail());
        User user = User.of(request, passwordEncoder, role);
        user = userRepository.save(user);

        sendVerificationEmail(user, apiGatewayUrl);
    }

    public void verify(@NotBlank String verificationCode) {
        User user = userRepository.getByVerificationCode(verificationCode);

        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException();
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void assertAccountNotExists(@NotBlank String username, @NotBlank String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AccountUsernameExistsException();
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AccountEmailExistsException();
        }
    }

    private void sendVerificationEmail(@NotNull User user, @NotBlank String verificationURL) {
        String verifyURL = verificationURL + "/user/verify/" + user.getVerificationCode();
        SendVerificationEmailEvent event = new SendVerificationEmailEvent(user.getUsername(), user.getEmail(), verifyURL);
        kafkaTemplate.send(SendVerificationEmailEvent.TOPIC_NAME, event);
    }
}
