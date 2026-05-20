package com.example.birthsafer.db.dao;

/*
 * BloodPressureDao.java
 * Description : 혈압 기록 DAO
 * Author       : 배서현
 * Contributors : 배서현
 * Created     : 2026-05-20
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 혈압 기록 DAO 작성 (2026.05.20 : 배서현)
 */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.birthsafer.db.entity.BloodPressureRecord;
import java.util.List;

@Dao
public interface BloodPressureDao {

    @Insert
    void insert(BloodPressureRecord record);

    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getByDate(int userId, String date);

    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getRecentRecords(int userId, String fromDate);

    @Query("SELECT MAX(systolic) FROM blood_pressure_records WHERE user_id = :userId AND date = :date")
    Integer getMaxSystolicByDate(int userId, String date);
}