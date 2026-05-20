package com.example.birthsafer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blood_pressure_records")
public class BloodPressureRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;         // 어떤 유저의 기록인지

    @ColumnInfo(name = "systolic")
    public int systolic;       // 수축기 혈압 (mmHg)

    @ColumnInfo(name = "diastolic")
    public int diastolic;      // 이완기 혈압 (mmHg)

    @ColumnInfo(name = "recorded_at")
    public String recordedAt;  // 기록 일시 (yyyy-MM-dd HH:mm:ss)

    @ColumnInfo(name = "date")
    public String date;        // 날짜만 (yyyy-MM-dd) - 캘린더 조회용
}