package com.utcn.app.model;

public enum RoleEnum {
    ADMIN("ADMIN"), TEACHER("TEACHER"), STUDENT("STUDENT");

    private final String type;

    RoleEnum(String string) {
        type = string;
    }

    @Override
    public String toString() {
        return type;
    }
}
