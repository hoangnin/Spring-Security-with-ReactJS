package com.lenin.securedoc.service;

import com.lenin.securedoc.dto.User;
import com.lenin.securedoc.entity.CredentialEntity;
import com.lenin.securedoc.entity.RoleEntity;
import com.lenin.securedoc.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);

    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);

    User getUserByUserId(String userId);

    User getUserByEmail(String email);

    CredentialEntity getUserCredentialById(Long id);
}
