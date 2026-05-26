package com.example.birthsafer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "symptom_records")
public class SymptomRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "nausea")
    public int nausea;         // 메스꺼움 심각도 (0~5, slider)

    @ColumnInfo(name = "fatigue")
    public int fatigue;        // 피로감 심각도 (0~5, slider2)

    @ColumnInfo(name = "headache")
    public int headache;       // 두통 심각도 (0~5, slider3)

    @ColumnInfo(name = "edema")
    public int edema;          // 부종 심각도 (0~5, slider4)

    @ColumnInfo(name = "dizziness")
    public int dizziness;      // 어지러움 심각도 (0~5, slider5)

    @ColumnInfo(name = "recorded_at")
    public String recordedAt;  // 기록 일시 (yyyy-MM-dd HH:mm:ss)

    @ColumnInfo(name = "date")
    public String date;        // 날짜만 (yyyy-MM-dd) - 캘린더 조회용
}