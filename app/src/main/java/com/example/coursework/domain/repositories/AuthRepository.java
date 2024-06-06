package com.example.coursework.domain.repositories;

import com.example.coursework.ui.entities.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<User> login(String email, String password);
    Single<Boolean> register(String name, String email, String password);
    Completable recoverPassword(String email);
    Completable changePassword(String email, String oldPassword, String newPassword);

    Completable changeName(String name);

    Single<User> getUser();
    Completable logout();
}
