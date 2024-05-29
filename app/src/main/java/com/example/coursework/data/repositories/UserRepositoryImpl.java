package com.example.coursework.data.repositories;

import com.example.coursework.App;
import com.example.coursework.data.database.UserDao;
import com.example.coursework.data.database.entities.UserEntity;
import com.example.coursework.domain.repositories.UserRepository;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao = App.getDb().getUserDao();

    @Override
    public Observable<List<User>> getAllUsers() {
        return userDao.getAllUsers().map(this::fromUserEntities);
    }

    @Override
    public Single<Integer> getUserCountByEmail(String email) {
        return userDao.getUserCountByEmail(email);
    }

    @Override
    public Single<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email).map(User::fromUserEntity);
    }

    @Override
    public Single<Integer> getUserByUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username);
    }

    @Override
    public Completable insertUser(User user) {
        return userDao.insertUser(user.toUserEntity());
    }

    @Override
    public Completable deleteUserById(int id) {
        return userDao.deleteUserById(id);
    }

    @Override
    public Completable updateUser(User user) {
        UserEntity userEntity = user.toUserEntity();
        return userDao.updateUser(userEntity);
    }

    @Override
    public Observable<User> getLoggedUser() {
        return userDao.getLoggedUser().map(User::fromUserEntity);
    }

    @Override
    public Completable logoutUser() {
        return userDao.logoutUser();
    }

    @Override
    public Completable loginUser(String username, String password) {
        return userDao.loginUser(username);
    }

    @Override
    public Observable<Integer> checkLoggedUser() {
        return userDao.checkLoggedUser();
    }

    @Override
    public Observable<UserType> getLoggedUserRole() {
        return userDao.getLoggedUserRole().map(UserType::fromInt);
    }

    private List<User> fromUserEntities(List<UserEntity> userEntities) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(User.fromUserEntity(userEntity));
        }
        return users;
    }
}
