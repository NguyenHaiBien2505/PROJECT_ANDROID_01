package com.example.ivymoda.DAO;
import androidx.room.*;

import com.example.ivymoda.Entity.VaiTro;

import java.util.List;

@Dao
public interface VaiTroDao {
    @Query("SELECT * FROM VaiTro") List<VaiTro> getAll();
    @Insert void insert(VaiTro vt);
    @Update void update(VaiTro vt);
    @Delete void delete(VaiTro vt);
    // Lấy vai trò theo ID
    @Query("SELECT * FROM vaitro WHERE maVaiTro = :id LIMIT 1")
    VaiTro getById(int id);
}