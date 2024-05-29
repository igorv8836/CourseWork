package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.UserEntity;
import com.example.coursework.domain.utils.UserType;


public class User {
    private String id;
    private String username;
    private String email;
    private UserType role;
    private boolean isLogged;

    public User(String id, String username, String email, UserType role, boolean isLogged) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isLogged = isLogged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public UserEntity toUserEntity() {
        return new UserEntity(id, username, email, role.getValue(), isLogged);
    }

    public static User fromUserEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                UserType.fromInt(userEntity.getRole()),
                userEntity.isLogged()
        );
    }
}
