package com.example.coursework.domain.repositories;

import com.example.coursework.data.firebase.AuthFirebase;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<Boolean> login(String email, String password);
    Single<Boolean> register(String email, String password);
    Completable recoverPassword(String email);
    Completable changePassword(String email, String oldPassword, String newPassword);
}
