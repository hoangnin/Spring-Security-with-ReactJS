package com.lenin.securedoc.utils;

import com.lenin.securedoc.entity.RoleEntity;
import com.lenin.securedoc.entity.UserEntity;

import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UserUtils {
    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName((lastName))
                .email(email)
                .lastLogin(now())
                .accountNonLocked(true)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://www.w3schools.com/w3images/avatar2.png")
                .role(role)
                .build();
    }
}
