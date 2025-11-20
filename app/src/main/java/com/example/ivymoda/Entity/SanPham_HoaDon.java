package com.example.ivymoda.Entity;
import androidx.room.*;

@Entity(tableName = "SanPham_HoaDon",
        primaryKeys = {"maSanPham", "maHoaDon"},
        foreignKeys = {
                @ForeignKey(entity = SanPham.class, parentColumns = "maSanPham", childColumns = "maSanPham"),
                @ForeignKey(entity = HoaDon.class, parentColumns = "maHoaDon", childColumns = "maHoaDon")
        })
public class SanPham_HoaDon {
    @ColumnInfo(index = true)
    public int maSanPham;
    @ColumnInfo(index = true)
    public int maHoaDon;
    public int soLuong = 1;
    public double giaBan;
}