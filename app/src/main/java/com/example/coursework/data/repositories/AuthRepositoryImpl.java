package com.example.coursework.data.repositories;

import com.example.coursework.data.firebase.AuthFirebase;
import com.example.coursework.domain.repositories.AuthRepository;
import com.example.coursework.ui.entities.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthFirebase authFirebase;

    public AuthRepositoryImpl() {
        this.authFirebase = new AuthFirebase();
    }

    @Override
    public Single<User> login(String email, String password) {
        return authFirebase.login(email, password);
    }

    @Override
    public Single<Boolean> register(String name, String email, String password) {
        return authFirebase.register(name, email, password);
    }

    @Override
    public Completable recoverPassword(String email) {
        return authFirebase.recoverPassword(email);
    }

    @Override
    public Completable changePassword(String email, String oldPassword, String newPassword) {
        return authFirebase.changePassword(email, oldPassword, newPassword);
    }

    @Override
    public Single<User> getUser() {
        return authFirebase.getUser();
    }

    @Override
    public Completable logout() {
        return authFirebase.logout();
    }
}
