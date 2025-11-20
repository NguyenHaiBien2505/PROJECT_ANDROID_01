package com.example.ivymoda.DAO;

import androidx.room.*;

import com.example.ivymoda.Entity.TaiKhoan;

import java.util.List;

@Dao
public interface TaiKhoanDao {
    @Query("SELECT * FROM TaiKhoan") List<TaiKhoan> getAll();
    @Query("SELECT * FROM TaiKhoan WHERE tenDangNhap = :username AND matKhau = :password LIMIT 1")
    TaiKhoan login(String username, String password);
    @Query("SELECT * FROM TaiKhoan WHERE tenDangNhap = :username LIMIT 1")
    TaiKhoan getByTenDangNhap(String username);
    @Insert void insert(TaiKhoan tk);
    @Update void update(TaiKhoan tk);
    @Delete void delete(TaiKhoan tk);

    @Insert
    void registerUser(TaiKhoan tk);

    @Query("SELECT * FROM taikhoan WHERE tenDangNhap = :uName AND matKhau = :pass LIMIT 1")
    TaiKhoan checkLogin(String uName, String pass);

    @Query("SELECT * FROM taikhoan WHERE tenDangNhap = :uName")
    TaiKhoan checkUsernameExist(String uName);
    @Query("SELECT * FROM taikhoan WHERE email = :mail")
    TaiKhoan checkEmailExist(String mail);
}