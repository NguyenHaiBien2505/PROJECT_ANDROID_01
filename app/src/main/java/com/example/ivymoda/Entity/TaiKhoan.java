package com.example.ivymoda.Entity;

import androidx.room.*;
import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "TaiKhoan", foreignKeys = @ForeignKey(entity = VaiTro.class, parentColumns = "maVaiTro", childColumns = "maVaiTro", onDelete = ForeignKey.CASCADE))
public class TaiKhoan implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int maTaiKhoan;

    public String matKhau;
    public String tenDangNhap;
    public String soDienThoai;
    public String email;
    public String hoTen;
    public Date ngaySinh;
    public String diaChi;
    public String gioiTinh; // "Nam", "Nữ"
    @ColumnInfo(index = true)
    public int maVaiTro;
    public Date ngayTao;

    public TaiKhoan() {
    }

    public TaiKhoan(int maTaiKhoan, String matKhau, String tenDangNhap, String email, String soDienThoai, Date ngaySinh, String hoTen, String diaChi, String gioiTinh, int maVaiTro, Date ngayTao) {
        this.maTaiKhoan = maTaiKhoan;
        this.matKhau = matKhau;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.maVaiTro = maVaiTro;
        this.ngayTao = ngayTao;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(int maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
}