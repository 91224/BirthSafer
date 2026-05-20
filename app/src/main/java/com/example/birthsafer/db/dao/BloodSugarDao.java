package com.example.birthsafer.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.birthsafer.db.entity.BloodSugarRecord;
import java.util.List;

@Dao
public interface BloodSugarDao {

    @Insert
    void insert(BloodSugarRecord record);

    // 특정 날짜의 기록 전체 조회
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getByDate(int userId, String date);

    // 최근 7일 기록 (AI용)
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getRecentRecords(int userId, String fromDate);

    // 공복/식후 구분 조회
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date AND is_fasting = :isFasting")
    List<BloodSugarRecord> getByDateAndType(int userId, String date, boolean isFasting);
}