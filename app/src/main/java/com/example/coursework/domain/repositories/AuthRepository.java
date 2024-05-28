package com.example.coursework.domain.repositories;

import com.example.coursework.data.firebase.AuthFirebase;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    private final AuthFirebase authFirebase;

    public AuthRepository() {
        this.authFirebase = new AuthFirebase();
    }

    public Single<Boolean> login(String email, String password) {
        return authFirebase.login(email, password);
    }

    public Single<Boolean> register(String email, String password) {
        return authFirebase.register(email, password);
    }

    public Completable recoverPassword(String email) {
        return authFirebase.recoverPassword(email);
    }

    public Completable changePassword(String email, String oldPassword, String newPassword) {
        return authFirebase.changePassword(email, oldPassword, newPassword);
    }
}
