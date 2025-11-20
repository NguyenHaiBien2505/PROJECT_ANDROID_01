package com.example.ivymoda.DAO;

import androidx.room.*;

import com.example.ivymoda.Entity.KhuyenMai_SanPham;

import java.util.List;

@Dao
public interface KhuyenMai_SanPhamDao {
    @Query("SELECT * FROM KhuyenMai_SanPham WHERE maKhuyenMai = :maKhuyenMai")
    List<KhuyenMai_SanPham> getByKhuyenMai(int maKhuyenMai);

    @Query("SELECT * FROM KhuyenMai_SanPham WHERE maSanPham = :maSanPham")
    List<KhuyenMai_SanPham> getBySanPham(int maSanPham);

    @Insert
    void insert(KhuyenMai_SanPham kmsp);

    @Update
    void update(KhuyenMai_SanPham kmsp);

    @Delete
    void delete(KhuyenMai_SanPham kmsp);
}