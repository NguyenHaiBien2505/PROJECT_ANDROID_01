package com.example.ivymoda.Entity;
import androidx.room.*;

@Entity(tableName = "GioHang_SanPham",
        primaryKeys = {"maGioHang", "maSanPham"},
        foreignKeys = {
                @ForeignKey(entity = GioHang.class, parentColumns = "maGioHang", childColumns = "maGioHang"),
                @ForeignKey(entity = SanPham.class, parentColumns = "maSanPham", childColumns = "maSanPham")
        })
public class GioHang_SanPham {
    @ColumnInfo(index = true)
    public int maGioHang;
    @ColumnInfo(index = true)
    public int maSanPham;
    public int soLuong = 1;
}