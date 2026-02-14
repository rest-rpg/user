package com.rest_rpg.user.config;

import com.ms.user.model.Role;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user")
@Getter
@Setter
public class UserProperties {

  private Set<Role> allowedRoles;
  private boolean registrationEnabled;
}
