package com.rest_rpg.user.service;

import com.rest_rpg.user.api.model.Role;
import com.rest_rpg.user.exception.AccountEmailExistsException;
import com.rest_rpg.user.exception.AccountUsernameExistsException;
import com.rest_rpg.user.exception.UserAlreadyVerifiedException;
import com.rest_rpg.user.exception.VerificationEmailSendErrorException;
import com.rest_rpg.user.model.User;
import com.rest_rpg.user.model.dto.RegisterRequest;
import com.rest_rpg.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void register(@NotNull RegisterRequest request,
                         @NotNull HttpServletRequest servletRequest,
                         @NotNull Role role) {
        assertAccountNotExists(request.getUsername(), request.getEmail());
        String verificationURL = getSiteURL(servletRequest);
        User user = User.of(request, passwordEncoder, role);
        user = userRepository.save(user);

        sendVerificationEmail(user, verificationURL);
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
        String toAddress = user.getEmail();
        String fromAddress = "server@restrpg.com";
        String senderName = "RPG";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Rest RPG.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", user.getUsername());
            String verifyURL = verificationURL + "/user/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationEmailSendErrorException();
        }

        mailSender.send(message);
    }

    private String getSiteURL(@NotNull HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
