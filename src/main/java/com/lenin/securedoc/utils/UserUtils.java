package com.lenin.securedoc.utils;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lenin.securedoc.dto.User;
import com.lenin.securedoc.entity.CredentialEntity;
import com.lenin.securedoc.entity.RoleEntity;
import com.lenin.securedoc.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

import static com.lenin.securedoc.constant.Constants.NINETY_DAYS;
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

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity userCredentialEntity){
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialNonExpired(isCredentialNonExpired(userCredentialEntity));
        user.setCreatedAt(userEntity.getCreateAt().toString());
        user.setUpdatedAt(userEntity.getUpdateAt().toString());
        user.setRole(role.getName());
        user.setAuthorities(role.getAuthorities().getValue());
        return user;
    }

    public static boolean isCredentialNonExpired(CredentialEntity userCredentialEntity) {
        return userCredentialEntity.getUpdateAt().plusDays(NINETY_DAYS).isAfter(now());
    }
}
