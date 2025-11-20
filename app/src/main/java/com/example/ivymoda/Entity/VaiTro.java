package com.example.ivymoda.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VaiTro")
public class VaiTro {
    @PrimaryKey(autoGenerate = true)
    public int maVaiTro;
    public String tenVaiTro;
}
