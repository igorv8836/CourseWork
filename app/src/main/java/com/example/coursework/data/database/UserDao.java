package com.example.coursework.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.coursework.data.database.entities.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    Observable<List<UserEntity>> getAllUsers();

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    Single<Integer> getUserCountByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email")
    Single<UserEntity> getUserByEmail(String email);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password")
    Single<Integer> getUserByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM users WHERE isLogged = 1")
    Observable<UserEntity> getLoggedUser();

    @Query("SELECT COUNT(*) FROM users WHERE isLogged = 1")
    Observable<Integer> checkLoggedUser();

    @Query("UPDATE users SET isLogged = 0")
    Completable logoutUser();

    @Query("UPDATE users SET isLogged = 1 WHERE username = :username AND password = :password")
    Completable loginUser(String username, String password);

    @Insert
    Completable insertUser(UserEntity user);

    @Query("DELETE FROM users WHERE id = :id")
    Completable deleteUserById(int id);

    @Update
    Completable updateUser(UserEntity user);

    @Query("SELECT role FROM users WHERE isLogged = 1")
    Observable<Integer> getLoggedUserRole();
}
