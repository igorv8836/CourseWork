package com.example.coursework.data.firebase.firestore;

import com.example.coursework.data.database.entities.UserEntity;
import com.example.coursework.data.firebase.AuthFirebase;
import com.example.coursework.domain.utils.UserType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UserFirestore {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth mAuth;
    private static final String USERS_COLLECTION = "users";


    public UserFirestore() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public Single<List<UserEntity>> getUsers(){
        return Single.create(emitter ->
                firestore.collection(USERS_COLLECTION)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<UserEntity> users = new ArrayList<>();
                            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(i);
                                UserEntity user = new UserEntity(
                                        doc.getString("name"),
                                        doc.getId(),
                                        doc.get("role") == null ? 2 : doc.getLong("role").intValue()
                                );
                                users.add(user);
                            }
                            emitter.onSuccess(users);
                        })
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Single<UserType> getUserType(){
        return Single.create(emitter ->
                firestore.collection(USERS_COLLECTION)
                        .document(mAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            UserType userType = UserType.fromInt(documentSnapshot.getLong("role").intValue());
                            emitter.onSuccess(userType);
                        })
                        .addOnFailureListener(emitter::onError)
        );
    }

    public Completable updateUser(UserEntity user){
        return Completable.create(emitter ->
                firestore.collection(USERS_COLLECTION)
                        .document(user.getEmail())
                        .update("name", user.getUsername(), "role", user.getRole())
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError)
        );
    }

}
