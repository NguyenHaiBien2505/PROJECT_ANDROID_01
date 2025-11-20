package com.example.ivymoda.DAO;
import androidx.room.*;

import com.example.ivymoda.Entity.GioHang_SanPham;

import java.util.List;

@Dao
public interface GioHang_SanPhamDao {
    @Query("SELECT * FROM GioHang_SanPham WHERE maGioHang = :maGioHang")
    List<GioHang_SanPham> getByGioHang(int maGioHang);

    @Insert
    void insert(GioHang_SanPham ghsp);

    @Update
    void update(GioHang_SanPham ghsp);

    @Delete
    void delete(GioHang_SanPham ghsp);

    @Query("DELETE FROM GioHang_SanPham WHERE maGioHang = :maGioHang")
    void clearGioHang(int maGioHang);
}