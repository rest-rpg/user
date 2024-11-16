package com.rest_rpg.user.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = UserInternalClient.class)
public class UserFeignConfigurer {
}
