package com.example.ivymoda.DAO;

import androidx.room.*;

import com.example.ivymoda.Entity.KhuyenMai;

import java.util.List;

@Dao
public interface KhuyenMaiDao {
    @Query("SELECT * FROM KhuyenMai WHERE thoiGianBatDau <= datetime('now') AND thoiGianKetThuc >= datetime('now')")
    List<KhuyenMai> getDangApDung();

    @Query("SELECT * FROM KhuyenMai")
    List<KhuyenMai> getAll();

    @Insert
    void insert(KhuyenMai km);

    @Update
    void update(KhuyenMai km);

    @Delete
    void delete(KhuyenMai km);
}