package com.example.coursework.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    private @NonNull String id;
    private String username;
    private String email;
    private int role;
    private boolean isLogged;

    public UserEntity(String username, String email, int role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.isLogged = false;
        id = "";
    }

    public UserEntity() {

    }


    public UserEntity(@NonNull String id, String username, String email, int role, boolean isLogged) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isLogged = isLogged;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}
