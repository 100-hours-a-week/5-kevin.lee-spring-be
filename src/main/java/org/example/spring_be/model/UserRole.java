package org.example.spring_be.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserRole {
    DEV("DEV"),
    ADMIN("ADMIN"),
    PENDING("PENDING"),
    VERIFIED("VERIFIED"),
    DENIED("DENIED");

    private final String value;

    UserRole(final String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
