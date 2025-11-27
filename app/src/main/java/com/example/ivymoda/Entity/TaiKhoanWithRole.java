package com.example.ivymoda.Entity;

public class TaiKhoanWithRole {
    public int maTaiKhoan;
    public String tenDangNhap;
    public String hoTen;
    public String email;
    public String tenVaiTro;

    public TaiKhoanWithRole() {
    }

    public TaiKhoanWithRole(int maTaiKhoan, String tenDangNhap, String hoTen, String email, String tenVaiTro) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
        this.email = email;
        this.tenVaiTro = tenVaiTro;
    }
}
