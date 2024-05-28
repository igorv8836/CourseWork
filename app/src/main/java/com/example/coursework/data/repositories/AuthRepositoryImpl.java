package com.example.coursework.data.repositories;

import com.example.coursework.data.firebase.AuthFirebase;
import com.example.coursework.domain.repositories.AuthRepository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthFirebase authFirebase;

    public AuthRepositoryImpl() {
        this.authFirebase = new AuthFirebase();
    }

    @Override
    public Single<Boolean> login(String email, String password) {
        return authFirebase.login(email, password);
    }

    @Override
    public Single<Boolean> register(String email, String password) {
        return authFirebase.register(email, password);
    }

    @Override
    public Completable recoverPassword(String email) {
        return authFirebase.recoverPassword(email);
    }

    @Override
    public Completable changePassword(String email, String oldPassword, String newPassword) {
        return authFirebase.changePassword(email, oldPassword, newPassword);
    }
}
