package com.example.ivymoda.DAO;
import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.ivymoda.Entity.SanPham;

import java.util.List;

@Dao
public interface SanPhamDao {
//    @Query("SELECT * FROM SanPham ORDER BY ngayTao DESC")
//    List<SanPham> getAll();
//
//    @Query("SELECT * FROM SanPham WHERE maDanhMuc = :maDanhMuc")
//    List<SanPham> getByDanhMuc(int maDanhMuc);
//
//    @Query("SELECT * FROM SanPham WHERE tenSanPham LIKE '%' || :keyword || '%'")
//    List<SanPham> search(String keyword);
//
//    @Insert
//    void insert(SanPham sp);
//
//    @Update
//    void update(SanPham sp);
//
//    @Delete
//    void delete(SanPham sp);

    @Insert
    long insert(SanPham product);

    @Update
    void update(SanPham product);

    @Delete
    void delete(SanPham product);

    @Query("DELETE FROM SanPham WHERE maSanPham = :maSanPham")
    void deleteById(int maSanPham);

    @Query("SELECT * FROM SanPham ORDER BY ngayTao DESC")
    LiveData<List<SanPham>> getAllSanPhams();

    @Query("SELECT * FROM SanPham ORDER BY ngayTao DESC")
    List<SanPham> getAll();

    @Query("SELECT * FROM SanPham WHERE maSanPham = :maSanPham")
    LiveData<SanPham> getSanPhamById(int maSanPham);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    @Query("SELECT * FROM SanPham WHERE LOWER(tenSanPham) LIKE '%' || LOWER(:query) || '%' ORDER BY ngayTao DESC")
    LiveData<List<SanPham>> searchSanPhams(String query);

    @Query("SELECT * FROM SanPham WHERE maDanhMuc = :maDanhMuc")
    LiveData<List<SanPham>> getSanPhamsByCategory(int maDanhMuc);

    @Query("SELECT COUNT(*) FROM SanPham")
    int getSanPhamCount();
}