// AppDatabase.java (ĐÃ SỬA – CHỈ DÙNG 2 BẢNG)
package com.example.ivymoda;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ivymoda.Convert.DateConverter;
import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.DAO.TaiKhoanDao;
import com.example.ivymoda.DAO.VaiTroDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.Entity.TaiKhoan;
import com.example.ivymoda.Entity.VaiTro;

@Database(entities = { SanPham.class, DanhMuc.class, TaiKhoan.class, VaiTro.class }, version = 2, exportSchema = false)
@TypeConverters({ DateConverter.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract SanPhamDao sanPhamDao();

    public abstract DanhMucDao danhMucDao();

    public abstract TaiKhoanDao taiKhoanDao();

    public abstract VaiTroDao vaiTroDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ivymoda.db")
                            .allowMainThreadQueries() // Tạm thời cho test
                            .fallbackToDestructiveMigration() // Cho phép xóa và tạo lại database khi version thay đổi
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}