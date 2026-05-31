package com.example.birthsafer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blood_sugar_records")
public class BloodSugarRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "blood_sugar")
    public int bloodSugar;     // 혈당 수치 (mg/dL)

    @ColumnInfo(name = "is_fasting")
    public boolean isFasting;  // true = 공복, false = 식후

    @ColumnInfo(name = "recorded_at")
    public String recordedAt;  // 기록 일시 (yyyy-MM-dd HH:mm:ss)

    @ColumnInfo(name = "date")
    public String date;        // 날짜만 (yyyy-MM-dd) - 캘린더 조회용
}
