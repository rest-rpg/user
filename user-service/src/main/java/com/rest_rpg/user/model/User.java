package com.rest_rpg.user.model;

import com.ms.user.model.CreateUserRequest;
import com.ms.user.model.Role;
import com.ms.user.model.UpdateOwnAccountRequest;
import com.ms.user.model.UserUpdateRequest;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {

  @Id @GeneratedValue private UUID id;

  @Column(unique = true)
  @NotBlank
  private String username;

  @Column(unique = true)
  @NotBlank
  @Email
  private String email;

  @NotBlank private String password;

  private boolean enabled;

  @Nullable
  @Size(max = 64)
  private String verificationCode;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

  private boolean deleted;

  public void update(@NotNull UserUpdateRequest userUpdateRequest) {
    username = userUpdateRequest.getUsername();
    email = userUpdateRequest.getEmail();
    role = userUpdateRequest.getRole();
  }

  public void update(@NotNull UpdateOwnAccountRequest request) {
    username = request.getUsername();
    email = request.getEmail();
  }

  public static User of(
      @NotNull CreateUserRequest createUserRequest,
      boolean enabled,
      @NotBlank String encodedPassword) {
    return User.builder()
        .username(createUserRequest.getRegisterRequest().getUsername())
        .email(createUserRequest.getRegisterRequest().getEmail())
        .password(encodedPassword)
        .role(createUserRequest.getRole())
        .enabled(enabled)
        .verificationCode(enabled ? null : UUID.randomUUID().toString())
        .deleted(false)
        .build();
  }

  public static User createDefaultAdmin(
      @NotBlank String username, @NotBlank String email, @NotBlank String encodedPassword) {
    return User.builder()
        .username(username)
        .email(email)
        .password(encodedPassword)
        .role(Role.ADMIN)
        .enabled(true)
        .deleted(false)
        .build();
  }

  @Override
  public Collection<SimpleGrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
