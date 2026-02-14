package com.rest_rpg.user.service;

import com.ms.user.model.CreateUserRequest;
import com.ms.user.model.RegisterRequest;
import com.ms.user.model.Role;
import com.ms.user.model.UpdateOwnAccountRequest;
import com.ms.user.model.UpdateOwnPasswordRequest;
import com.ms.user.model.UpdatePasswordRequest;
import com.ms.user.model.UserFilter;
import com.ms.user.model.UserLite;
import com.ms.user.model.UserLitePage;
import com.ms.user.model.UserUpdateRequest;
import com.rest_rpg.mailer_api.model.SendVerificationEmailEvent;
import com.rest_rpg.user.config.UserProperties;
import com.rest_rpg.user.exception.AccountEmailExistsException;
import com.rest_rpg.user.exception.AccountUsernameExistsException;
import com.rest_rpg.user.exception.IncorrectOldPasswordException;
import com.rest_rpg.user.exception.UserAlreadyVerifiedException;
import com.rest_rpg.user.exception.UserRegistrationNotEnabledException;
import com.rest_rpg.user.exception.UserRoleNotAllowedException;
import com.rest_rpg.user.mapper.UserMapper;
import com.rest_rpg.user.model.User;
import com.rest_rpg.user.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final UserProperties userProperties;
  private final UserMapper userMapper;

  @Value("${api-gateway.url}")
  private String apiGatewayUrl;

  public void register(@NotNull @Valid RegisterRequest request) {
    if (!userProperties.isRegistrationEnabled()) {
      throw new UserRegistrationNotEnabledException();
    }
    User user = createUser(new CreateUserRequest(request, Role.USER), false);

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

  public User createUser(@NotNull @Valid CreateUserRequest createUserRequest, boolean userEnabled) {
    if (!userProperties.getAllowedRoles().contains(createUserRequest.getRole())) {
      throw new UserRoleNotAllowedException();
    }
    assertAccountNotExists(
        createUserRequest.getRegisterRequest().getUsername(),
        createUserRequest.getRegisterRequest().getEmail());
    User user =
        User.of(
            createUserRequest,
            userEnabled,
            passwordEncoder.encode(createUserRequest.getRegisterRequest().getPassword()));
    return userRepository.save(user);
  }

  public void deleteUser(@NotNull UUID userId) {
    User user = userRepository.getByIdAndDeletedFalse(userId);
    user.setDeleted(false);
    userRepository.save(user);
  }

  public UserLitePage findUsers(@NotNull @Valid UserFilter filter, @NotNull Pageable pageable) {
    Page<User> users = userRepository.findUsers(filter, pageable);
    return userMapper.toUserLitePage(users);
  }

  public UserLite getUser(@NotNull UUID userId) {
    User user = userRepository.getByIdAndDeletedFalse(userId);
    return userMapper.toUserLite(user);
  }

  public UserLite updateUserByAdmin(
      @NotNull UUID userId, @NotNull @Valid UserUpdateRequest userUpdateRequest) {
    User user = userRepository.getByIdAndDeletedFalse(userId);
    return updateUser(userUpdateRequest, user);
  }

  public void updateUserPasswordByAdmin(
      @NotNull UUID userId, @NotNull @Valid UpdatePasswordRequest updatePasswordRequest) {
    User user = userRepository.getByIdAndDeletedFalse(userId);
    updateUserPassword(user, updatePasswordRequest.getNewPassword());
  }

  public UserLite updateOwnAccount(
      @NotBlank String username, @NotNull @Valid UpdateOwnAccountRequest request) {
    User user = userRepository.getByUsername(username);
    user.update(request);
    User updatedUser = userRepository.save(user);
    return userMapper.toUserLite(updatedUser);
  }

  public void updateOwnPassword(
      @NotBlank String username,
      @NotNull @Valid UpdateOwnPasswordRequest updateOwnPasswordRequest) {
    User user = userRepository.getByUsername(username);
    if (!passwordEncoder.matches(updateOwnPasswordRequest.getOldPassword(), user.getPassword())) {
      throw new IncorrectOldPasswordException();
    }
    updateUserPassword(user, updateOwnPasswordRequest.getNewPassword());
  }

  private void updateUserPassword(@NotNull User user, @NotBlank String updateOwnPasswordRequest) {
    user.setPassword(passwordEncoder.encode(updateOwnPasswordRequest));
    userRepository.save(user);
  }

  private UserLite updateUser(
      @NotNull @Valid UserUpdateRequest userUpdateRequest, @NotNull User user) {
    user.update(userUpdateRequest);
    User updatedUser = userRepository.save(user);
    return userMapper.toUserLite(updatedUser);
  }

  private void assertAccountNotExists(@NotBlank String username, @NotBlank String email) {
    if (userRepository.findByUsernameAndDeletedFalse(username).isPresent()) {
      throw new AccountUsernameExistsException();
    }
    if (userRepository.findByEmailAndDeletedFalse(email).isPresent()) {
      throw new AccountEmailExistsException();
    }
  }

  private void sendVerificationEmail(@NotNull User user, @NotBlank String apiGatewayUrl) {
    String verifyURL = apiGatewayUrl + "/user/verify/" + user.getVerificationCode();
    SendVerificationEmailEvent event =
        new SendVerificationEmailEvent(user.getUsername(), user.getEmail(), verifyURL);
    kafkaTemplate.send(SendVerificationEmailEvent.TOPIC_NAME, event);
  }
}
