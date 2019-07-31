package com.example.lyrio.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lyrio.models.User;

import java.util.List;

import io.reactivex.Flowable;

public interface UserDao {


    void inserir(User user);

    void delete(User user);

    void update(User user);

    @Query("SELECT * FROM user")
    Flowable<List<User>> getAll();

    @Insert
    void inserirUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);

}
