package com.example.coursework.data.repositories;

import com.example.coursework.data.database.entities.UserEntity;
import com.example.coursework.data.firebase.firestore.UserFirestore;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImpl implements UserRepository {
    private final UserFirestore userFirestore = new UserFirestore();

    @Override
    public Single<List<User>> getAllUsers() {
        return userFirestore.getUsers().map(this::fromUserEntities);
    }

    @Override
    public Completable updateUser(User user) {
        UserEntity userEntity = user.toUserEntity();
        return userFirestore.updateUser(userEntity);
    }

    @Override
    public Single<UserType> getLoggedUserType() {
        return userFirestore.getUserType();
    }

    private List<User> fromUserEntities(List<UserEntity> userEntities) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(User.fromUserEntity(userEntity));
        }
        return users;
    }
}
