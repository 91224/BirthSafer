package com.example.birthsafer.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.birthsafer.db.entity.BloodPressureRecord;
import java.util.List;

@Dao
public interface BloodPressureDao {

    @Insert
    void insert(BloodPressureRecord record);

    // 특정 날짜의 기록 전체 조회
    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getByDate(int userId, String date);

    // 최근 7일 기록 (AI용)
    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getRecentRecords(int userId, String fromDate);

    // 특정 날짜의 최대값 (캘린더 인디케이터용)
    @Query("SELECT MAX(systolic) FROM blood_pressure_records WHERE user_id = :userId AND date = :date")
    Integer getMaxSystolicByDate(int userId, String date);
}