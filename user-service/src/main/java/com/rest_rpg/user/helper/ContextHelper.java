package com.rest_rpg.user.helper;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class ContextHelper {

  public static String getUsernameFromContext() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
