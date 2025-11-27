package com.example.ivymoda;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ivymoda.Convert.DateConverter;
import com.example.ivymoda.DAO.DanhMucDao;
import com.example.ivymoda.DAO.SanPhamDao;
import com.example.ivymoda.DAO.TaiKhoanDao;
import com.example.ivymoda.DAO.VaiTroDao;
import com.example.ivymoda.Entity.DanhMuc;
import com.example.ivymoda.Entity.SanPham;
import com.example.ivymoda.Entity.TaiKhoan;
import com.example.ivymoda.Entity.VaiTro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = { SanPham.class, DanhMuc.class, TaiKhoan.class, VaiTro.class }, version = 2, exportSchema = false)
@TypeConverters({ DateConverter.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract SanPhamDao sanPhamDao();
    public abstract DanhMucDao danhMucDao();
    public abstract TaiKhoanDao taiKhoanDao();
    public abstract VaiTroDao vaiTroDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ivymoda.db")
                            .addCallback(getRoomDatabaseCallback(context.getApplicationContext())) // Sử dụng callback
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback getRoomDatabaseCallback(final Context context) {
        return new RoomDatabase.Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) { // Sử dụng onOpen
                super.onOpen(db);
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase database = INSTANCE;
                    if (database != null) {
                        // Lấy các DAO
                        VaiTroDao vtDao = database.vaiTroDao();
                        TaiKhoanDao tkDao = database.taiKhoanDao();
                        DanhMucDao dmDao = database.danhMucDao();
                        SanPhamDao spDao = database.sanPhamDao();

                        // --- CHÈN DỮ LIỆU NẾU BẢNG TRỐNG ---

                        if (vtDao.getAll().isEmpty()) {
                            VaiTro admin = new VaiTro();
                            admin.tenVaiTro = "Admin";
                            vtDao.insert(admin);

                            VaiTro user = new VaiTro();
                            user.tenVaiTro = "User";
                            vtDao.insert(user);
                        }

                        if (tkDao.checkUsernameExist("admin") == null) {
                            TaiKhoan adminAccount = new TaiKhoan();
                            adminAccount.tenDangNhap = "admin";
                            adminAccount.matKhau = "admin123";
                            adminAccount.hoTen = "Administrator";
                            adminAccount.email = "admin@ivymoda.com";
                            adminAccount.maVaiTro = 1; // Giả định ID của Admin là 1
                            adminAccount.ngayTao = new Date();
                            tkDao.registerUser(adminAccount);
                        }

                        if (dmDao.getAllCategories().isEmpty()) {
                            DanhMuc dm1 = new DanhMuc();
                            dm1.tenDanhMuc = "Áo";
                            dmDao.insertCategory(dm1);

                            DanhMuc dm2 = new DanhMuc();
                            dm2.tenDanhMuc = "Quần";
                            dmDao.insertCategory(dm2);

                            DanhMuc dm3 = new DanhMuc();
                            dm3.tenDanhMuc = "Váy";
                            dmDao.insertCategory(dm3);
                        }

                        if (spDao.getAll().isEmpty()) {
                            List<SanPham> samples = new ArrayList<>();

                            SanPham sp1 = new SanPham();
                            sp1.hinhAnh = R.drawable.ao1;
                            sp1.tenSanPham = "Áo Sơ Mi Trắng Ivy";
                            sp1.moTa = "Chất cotton thoáng mát";
                            sp1.giaBan = 890000.0;
                            sp1.soLuong = 50;
                            sp1.mauSac = "Trắng, Đen";
                            sp1.size = "S, M, L";
                            sp1.maDanhMuc = 1;
                            sp1.ngayTao = new Date();
                            samples.add(sp1);

                            SanPham sp2 = new SanPham();
                            sp2.hinhAnh = R.drawable.vay1;
                            sp2.tenSanPham = "Váy Xòe Hoa Nhí";
                            sp2.moTa = "Thiết kế trẻ trung";
                            sp2.giaBan = 1290000.0;
                            sp2.soLuong = 30;
                            sp2.mauSac = "Hồng,Vàng";
                            sp2.size = "S, M, L";
                            sp2.maDanhMuc = 3;
                            sp2.ngayTao = new Date();
                            samples.add(sp2);

                            SanPham sp3 = new SanPham();
                            sp3.hinhAnh = R.drawable.quan1;
                            sp3.tenSanPham = "Quần Jeans Ống Rộng";
                            sp3.moTa = "Phong cách Hàn Quốc";
                            sp3.giaBan = 990000.0;
                            sp3.soLuong = 40;
                            sp3.mauSac = "Xanh, Xám";
                            sp3.size = "28, 29, 30";
                            sp3.maDanhMuc = 2;
                            sp3.ngayTao = new Date();
                            samples.add(sp3);

                            for (SanPham sp : samples) {
                                spDao.insert(sp);
                            }
                        }
                    }
                });
            }
        };
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}