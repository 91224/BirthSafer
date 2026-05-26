package com.example.birthsafer.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.birthsafer.db.entity.SymptomRecord;
import java.util.List;

@Dao
public interface SymptomDao {

    @Insert
    void insert(SymptomRecord record);

    // 특정 날짜의 기록 조회하기
    @Query("SELECT * FROM symptom_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<SymptomRecord> getByDate(int userId, String date);

    // 최근 7일 기록 (AI리포트)
    @Query("SELECT * FROM symptom_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<SymptomRecord> getRecentRecords(int userId, String fromDate);
}
