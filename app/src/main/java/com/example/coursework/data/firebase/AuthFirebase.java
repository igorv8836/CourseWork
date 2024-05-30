package com.example.coursework.data.firebase;

import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthFirebase {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public AuthFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public Single<User> getUser() {
        return Single.create(emitter -> {
            if (mAuth.getCurrentUser() != null) {
                User user = new User(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), UserType.USER, //TODO("Указать тип аккаунта")
                        true);
                emitter.onSuccess(user);
            } else {
                emitter.onError(new IllegalArgumentException("Пользователь не найден"));
            }
        });
    }

    public Single<User> login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return Single.error(new IllegalArgumentException("Параметр не может быть пустым"));
        }

        return Single.create(emitter -> {
            DocumentReference docRef = db.collection("users").document(email);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    DocumentSnapshot docSnap = task.getResult();
                    User user = new User(docSnap.getString("uid"), docSnap.getString("name"), docSnap.getString(email), UserType.fromInt(((Long) Objects.requireNonNull(docSnap.get("role"))).intValue()), true);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            emitter.onSuccess(user);
                        } else {
                            emitter.onError(Objects.requireNonNull(authTask.getException()));
                        }
                    });
                } else {
                    emitter.onError(new IllegalArgumentException("Такой пользователь не найден"));
                }
            });
        });
    }

    public Single<Boolean> register(String name, String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return Single.error(new IllegalArgumentException("Параметр не может быть пустым"));
        }

        DocumentReference userDocRef = db.collection("users").document(email);
        return Single.create(emitter -> userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    emitter.onError(new IllegalArgumentException("Пользователь с таким email уже существует"));
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(profileUpdateTask -> {
                                    if (profileUpdateTask.isSuccessful()) {
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("role", 2);
                                        userData.put("name", name);

                                        userDocRef.set(userData).addOnCompleteListener(addTask -> {
                                            if (addTask.isSuccessful()) {
                                                emitter.onSuccess(true);
                                            } else {
                                                emitter.onError(Objects.requireNonNull(addTask.getException()));
                                            }
                                        });
                                    } else {
                                        emitter.onError(Objects.requireNonNull(profileUpdateTask.getException()));
                                    }
                                });
                            } else {
                                emitter.onError(new IllegalStateException("Пользователь не аутентифицирован"));
                            }
                        } else {
                            emitter.onError(Objects.requireNonNull(authTask.getException()));
                        }
                    });
                }
            } else {
                emitter.onError(Objects.requireNonNull(task.getException()));
            }
        }));
    }


    public Completable recoverPassword(String email) {
        if (email.isEmpty()) {
            return Completable.error(new IllegalArgumentException("Параметр не может быть пустым"));
        }
        return Completable.create(emitter -> mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                emitter.onComplete();
            } else {
                emitter.onError(Objects.requireNonNull(task.getException()));
            }
        }));
    }

    public Completable changePassword(String email, String oldPassword, String newPassword) {
        if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            return Completable.error(new IllegalArgumentException("Параметр не может быть пустым"));
        }
        return Completable.create(emitter -> {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            Objects.requireNonNull(mAuth.getCurrentUser()).reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(updateTask -> {
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

    public Completable logout() {
        return Completable.create(emitter -> {
            mAuth.signOut();
            emitter.onComplete();
        });
    }

    public Completable changeName(String name) {
        return Completable.create(emitter -> {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onComplete();
                } else {
                    emitter.onError(Objects.requireNonNull(task.getException()));
                }
            });
        });
    }
}
