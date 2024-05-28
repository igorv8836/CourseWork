package com.example.coursework.data.firebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthFirebase {
    private final FirebaseAuth mAuth;

    public AuthFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Single<Boolean> login(String email, String password) {
        return Single.create(emitter ->
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onSuccess(true);
                            } else {
                                emitter.onError(Objects.requireNonNull(task.getException()));
                            }
                        })
        );
    }

    public Single<Boolean> register(String email, String password) {
        return Single.create(emitter ->
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onSuccess(true);
                            } else {
                                emitter.onError(Objects.requireNonNull(task.getException()));
                            }
                        })
        );
    }

    public Completable recoverPassword(String email) {
        return Completable.create(emitter ->
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(Objects.requireNonNull(task.getException()));
                            }
                        })
        );
    }

    public Completable changePassword(String email, String oldPassword, String newPassword) {
        return Completable.create(emitter -> {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            Objects.requireNonNull(mAuth.getCurrentUser()).reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            emitter.onComplete();
                                        } else {
                                            emitter.onError(Objects.requireNonNull(updateTask.getException()));
                                        }
                                    });
                        } else {
                            emitter.onError(Objects.requireNonNull(task.getException()));
                        }
                    });
        });
    }
}
