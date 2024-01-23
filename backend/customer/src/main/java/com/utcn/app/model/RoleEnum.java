package com.utcn.app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum RoleEnum {
    ADMIN("ADMIN"), USER("USER");

    private final String type;

    RoleEnum(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }
}