package com.example.ivymoda.Entity;
import androidx.room.*;
import java.util.Date;

@Entity(tableName = "HoaDon",
        foreignKeys = @ForeignKey(entity = TaiKhoan.class,
                parentColumns = "maTaiKhoan",
                childColumns = "maTaiKhoan",
                onDelete = ForeignKey.CASCADE))
public class HoaDon {
    @PrimaryKey(autoGenerate = true)
    public int maHoaDon;

    public String trangThai;
    public String diaChiNhanHang;
    public Date thoiGianNhanHang;
    public double tongTien;
    @ColumnInfo(index = true)
    public int maTaiKhoan;
    public Date ngayTao;
}