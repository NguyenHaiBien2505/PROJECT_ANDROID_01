package com.example.ivymoda.DAO;


import androidx.room.*;

import com.example.ivymoda.Entity.SanPham_HoaDon;

import java.util.List;

@Dao
public interface SanPham_HoaDonDao {
    @Query("SELECT * FROM SanPham_HoaDon WHERE maHoaDon = :maHoaDon")
    List<SanPham_HoaDon> getByHoaDon(int maHoaDon);

    @Insert
    void insert(SanPham_HoaDon sphd);

    @Update
    void update(SanPham_HoaDon sphd);

    @Delete
    void delete(SanPham_HoaDon sphd);
}