package com.example.coursework.domain.repositories;

import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Single<List<User>> getAllUsers();

    Completable updateUser(User user);
    Single<UserType> getLoggedUserType();
//    Single<Integer> getUserCountByEmail(String email);
//    Single<User> getUserByEmail(String email);
//    Single<Integer> getUserByUsernameAndPassword(String username, String password);
//    Completable insertUser(User user);
//    Completable deleteUserById(int id);
//    Completable updateUser(User user);
//    Completable logoutUser();
//    Completable loginUser(String username, String password);
//    Observable<Integer> checkLoggedUser();
//    Observable<UserType> getLoggedUserRole();
}
