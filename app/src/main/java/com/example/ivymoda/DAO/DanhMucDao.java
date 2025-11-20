package com.example.ivymoda.DAO;
import androidx.room.*;

import com.example.ivymoda.Entity.DanhMuc;

import java.util.List;

@Dao
public interface DanhMucDao {
    @Insert
    void insertCategory(DanhMuc dm);
    @Update
    void updateCategory(DanhMuc dm);

    @Query("SELECT * FROM danhmuc")
    List<DanhMuc> getAllCategories();

    @Query("SELECT * FROM danhmuc WHERE maDanhMuc = :id")
    DanhMuc getCategoryById(int id);

    // Bạn có thể thêm các hàm khác sau này như delete, update...
    @Query("DELETE FROM danhmuc WHERE maDanhMuc = :id")
    void deleteCategory(int id);
}