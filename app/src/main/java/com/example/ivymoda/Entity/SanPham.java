package com.example.ivymoda.Entity;

import androidx.room.*;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "SanPham",
        foreignKeys = @ForeignKey(entity = DanhMuc.class,
                parentColumns = "maDanhMuc",
                childColumns = "maDanhMuc",
                onDelete = ForeignKey.CASCADE))
public class SanPham implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int maSanPham;

    @ColumnInfo(name = "hinhAnh")
    public int hinhAnh; // Resource ID của ảnh. Lưu ý: chỉ dùng cho ảnh có sẵn trong project.

    public String tenSanPham;
    public String moTa;
    public double giaBan;
    public int soLuong;
    public String mauSac;
    public String size;

    @ColumnInfo(index = true)
    public int maDanhMuc;

    public Date ngayTao;

    // Constructor đầy đủ
    @Ignore
    public SanPham(int maSanPham, int hinhAnh, String tenSanPham, String moTa, double giaBan, int soLuong,
                   String mauSac, String size, int maDanhMuc, Date ngayTao) {
        this.maSanPham = maSanPham;
        this.hinhAnh = hinhAnh;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.mauSac = mauSac;
        this.size = size;
        this.maDanhMuc = maDanhMuc;
        this.ngayTao = ngayTao;
    }

    // Constructor rỗng cho Room
    public SanPham() {}

    // Getters & Setters
    public int getMaSanPham() { return maSanPham; }
    public void setMaSanPham(int maSanPham) { this.maSanPham = maSanPham; }

    public int getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(int hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
}