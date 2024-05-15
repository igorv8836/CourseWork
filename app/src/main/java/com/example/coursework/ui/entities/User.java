package com.example.coursework.ui.entities;

import com.example.coursework.data.database.entities.UserEntity;
import com.example.coursework.domain.utils.UserType;


public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private UserType role;
    private boolean isLogged;

    public User(int id, String username, String password, String email, UserType role, boolean isLogged) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isLogged = isLogged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return new UserEntity(id, username, password, email, role.getValue(), isLogged);
    }

    public static User fromUserEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                UserType.fromInt(userEntity.getRole()),
                userEntity.isLogged()
        );
    }
}
