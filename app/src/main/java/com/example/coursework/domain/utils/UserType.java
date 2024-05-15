package com.example.coursework.domain.utils;

public enum UserType {
    CREATOR(0),
    ADMIN(1),
    USER(2);

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserType fromInt(int value) {
        for (UserType userType : UserType.values()) {
            if (userType.getValue() == value) {
                return userType;
            }
        }
        return null;
    }
}
