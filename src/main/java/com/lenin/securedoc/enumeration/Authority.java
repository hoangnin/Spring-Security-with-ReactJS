package com.lenin.securedoc.enumeration;

import static com.lenin.securedoc.constant.Constants.*;

public enum Authority {
    user(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES);

    private final String value;

    Authority(String value) {
        this.value = value;
    }
    public String getValue(){return this.value; }

}
