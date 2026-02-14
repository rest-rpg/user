package com.rest_rpg.user.mapper;

import com.ms.user.model.PageMetadata;
import com.ms.user.model.UserLite;
import com.ms.user.model.UserLitePage;
import com.ms.user.model.UserWithPassword;
import com.rest_rpg.user.model.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserLite toUserLite(@NotNull User user);

  UserWithPassword toUserWithPassword(@NotNull User user);

  List<UserLite> toUserLiteList(@NotNull List<User> users);

  @Mapping(target = "data", source = "userPage.content")
  @Mapping(target = "meta", source = "userPage")
  UserLitePage toUserLitePage(@NotNull Page<User> userPage);

  default PageMetadata toMetadata(@NotNull Page<?> page) {
    PageMetadata meta = new PageMetadata();
    meta.setPage(page.getNumber());
    meta.setSize(page.getSize());
    meta.setTotalElements(page.getTotalElements());
    meta.setTotalPages(page.getTotalPages());
    return meta;
  }
}
