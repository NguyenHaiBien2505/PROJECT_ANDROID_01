package com.example.ivymoda.Entity;

import androidx.room.*;

@Entity(tableName = "KhuyenMai_SanPham",
        primaryKeys = {"maKhuyenMai", "maSanPham"},
        foreignKeys = {
                @ForeignKey(entity = KhuyenMai.class, parentColumns = "maKhuyenMai", childColumns = "maKhuyenMai"),
                @ForeignKey(entity = SanPham.class, parentColumns = "maSanPham", childColumns = "maSanPham")
        })
public class KhuyenMai_SanPham {
    @ColumnInfo(index = true)
    public int maKhuyenMai;
    @ColumnInfo(index = true)
    public int maSanPham;
}