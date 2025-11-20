package com.example.ivymoda.DAO;

import androidx.room.*;

import com.example.ivymoda.Entity.GioHang;

import java.util.List;

@Dao
public interface GioHangDao {
    @Query("SELECT * FROM GioHang WHERE maTaiKhoan = :maTaiKhoan")
    GioHang getByTaiKhoan(int maTaiKhoan);

    @Insert
    void insert(GioHang gh);

    @Update
    void update(GioHang gh);

    @Delete
    void delete(GioHang gh);
}