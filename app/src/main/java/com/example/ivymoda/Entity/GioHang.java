package com.example.ivymoda.Entity;
import androidx.room.*;
import java.util.Date;

@Entity(tableName = "GioHang",
        foreignKeys = @ForeignKey(entity = TaiKhoan.class,
                parentColumns = "maTaiKhoan",
                childColumns = "maTaiKhoan",
                onDelete = ForeignKey.CASCADE))
public class GioHang {
    @PrimaryKey(autoGenerate = true)
    public int maGioHang;
    @ColumnInfo(index = true)
    public int maTaiKhoan;
    public Date ngayTao;
}