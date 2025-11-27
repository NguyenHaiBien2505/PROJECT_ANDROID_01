package com.example.ivymoda.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VaiTro")
public class VaiTro {
    @PrimaryKey(autoGenerate = true)
    public int maVaiTro;
    public String tenVaiTro;

    public String getTenVaiTro() {
        return tenVaiTro;
    }

    public VaiTro() {
    }

    public VaiTro(int maVaiTro, String tenVaiTro) {
        this.maVaiTro = maVaiTro;
        this.tenVaiTro = tenVaiTro;
    }

    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }

    public int getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(int maVaiTro) {
        this.maVaiTro = maVaiTro;
    }
}
