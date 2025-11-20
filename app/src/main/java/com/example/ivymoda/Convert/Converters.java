package com.example.ivymoda.Convert;

import androidx.room.TypeConverter;
import java.util.Date;

public class Converters {

    // Chuyển Date → Long (lưu vào DB)
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    // Chuyển Long → Date (lấy từ DB)
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}