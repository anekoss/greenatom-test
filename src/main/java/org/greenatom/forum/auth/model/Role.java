package org.greenatom.forum.auth.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ROLE_ADMIN("ADMIN"),
    ROLE_CLIENT("CLIENT");
    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
