package com.example.ivymoda;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.example.ivymoda.DAO.*;
import com.example.ivymoda.Entity.*;

import java.util.List;

public class DatabaseAsync {

    // ---------- CALLBACK ----------
    public interface ListCallback<T> { void onResult(List<T> list); }
    public interface SingleCallback<T> { void onResult(T item); }
    public interface Done { void run(); }

    // ---------- VAI TRÒ ----------
    public static void getAllVaiTro(VaiTroDao dao, ListCallback<VaiTro> cb) {
        new AsyncTask<Void, Void, List<VaiTro>>() {
            @Override protected List<VaiTro> doInBackground(Void... v) { return dao.getAll(); }
            @Override protected void onPostExecute(List<VaiTro> list) { cb.onResult(list); }
        }.execute();
    }

    // ---------- TÀI KHOẢN ----------
    public static void insertTaiKhoan(TaiKhoanDao dao, TaiKhoan tk, Done done) {
        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... v) { dao.insert(tk); return null; }
            @Override protected void onPostExecute(Void v) { if (done != null) done.run(); }
        }.execute();
    }

    public static void getTaiKhoanByUsername(TaiKhoanDao dao, String username, SingleCallback<TaiKhoan> cb) {
        new AsyncTask<Void, Void, TaiKhoan>() {
            @Override protected TaiKhoan doInBackground(Void... v) { return dao.getByTenDangNhap(username); }
            @Override protected void onPostExecute(TaiKhoan tk) { cb.onResult(tk); }
        }.execute();
    }

    // ---------- SẢN PHẨM ----------
    public static void getAllSanPham(SanPhamDao dao, ListCallback<SanPham> cb) {
        new AsyncTask<Void, Void, List<SanPham>>() {
            @Override protected List<SanPham> doInBackground(Void... v) { return dao.getAll(); }
            @Override protected void onPostExecute(List<SanPham> list) { cb.onResult(list); }
        }.execute();
    }

    public static void insertSanPham(SanPhamDao dao, SanPham sp, Done done) {
        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... v) { dao.insert(sp); return null; }
            @Override protected void onPostExecute(Void v) {
                if (done != null) {
                    done.run(); // Chạy trên Main Thread
                }
            }
        }.execute();
    }

    // ... (tương tự cho các DAO còn lại: DanhMuc, GioHang, HoaDon, KhuyenMai, ...)
}