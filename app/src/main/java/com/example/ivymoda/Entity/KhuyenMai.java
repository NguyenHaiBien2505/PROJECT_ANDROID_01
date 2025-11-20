package com.example.ivymoda.Entity;

import androidx.room.*;
import java.util.Date;

@Entity(tableName = "KhuyenMai")
public class KhuyenMai {
    @PrimaryKey(autoGenerate = true)
    public int maKhuyenMai;

    public String tenKhuyenMai;
    public double phanTramGiam;
    public Date thoiGianBatDau;
    public Date thoiGianKetThuc;
    public Date ngayTao;
}