package com.example.birthsafer.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.birthsafer.db.entity.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    // 아이디 중복확인 / 로그인 조회
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    // 로그인
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    // userId로 조회 (마이페이지)
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User findById(int userId);

    // 유저 정보 수정
    @Update
    void updateUser(User user);
}
