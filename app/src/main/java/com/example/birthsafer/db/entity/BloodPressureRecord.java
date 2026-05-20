package com.example.birthsafer.db.entity;

/*
 * BloodPressureRecord.java
 * Description : 혈압 기록 테이블 엔티티
 * Author       : 배서현
 * Contributors : 배서현
 * Created     : 2026-05-20
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 혈압 기록 엔티티 작성 (2026.05.20 : 배서현)
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blood_pressure_records")
public class BloodPressureRecord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "systolic")
    public int systolic;

    @ColumnInfo(name = "diastolic")
    public int diastolic;

    @ColumnInfo(name = "recorded_at")
    public String recordedAt;

    @ColumnInfo(name = "date")
    public String date;
}