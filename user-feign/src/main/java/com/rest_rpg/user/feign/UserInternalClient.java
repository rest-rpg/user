package com.rest_rpg.user.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "user", url = "http://localhost:8081")
public interface UserInternalClient extends UserInternalApi {
}
