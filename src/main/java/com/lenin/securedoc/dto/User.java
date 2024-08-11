package com.lenin.securedoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lenin.securedoc.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;

@Data
public class User {
    private Long id;
    private Long createdBy;
    private Long updatedBy;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private String imageUrl;
    private String qrCodeImageUri;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialNonExpired;
    private boolean enabled;
    private boolean mfa;

    public User(Long id, String firstName, String lastName, String email) {
    }

    public User() {

    }
}
