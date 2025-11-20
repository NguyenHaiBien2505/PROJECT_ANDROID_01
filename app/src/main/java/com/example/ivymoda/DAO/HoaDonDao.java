package com.example.ivymoda.DAO;

import androidx.room.*;

import com.example.ivymoda.Entity.HoaDon;

import java.util.List;

@Dao
public interface HoaDonDao {
    @Query("SELECT * FROM HoaDon WHERE maTaiKhoan = :maTaiKhoan ORDER BY ngayTao DESC")
    List<HoaDon> getByTaiKhoan(int maTaiKhoan);

    @Insert
    void insert(HoaDon hd);

    @Update
    void update(HoaDon hd);

    @Delete
    void delete(HoaDon hd);
}