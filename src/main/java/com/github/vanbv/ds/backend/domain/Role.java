package com.github.vanbv.ds.backend.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    COURIER,
    OPERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
