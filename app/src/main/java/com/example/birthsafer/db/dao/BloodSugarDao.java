package com.example.birthsafer.db.dao;

/*
 * BloodSugarDao.java
 * Description : 혈당 기록 DAO
 * Author       : 배서현
 * Contributors : 배서현
 * Created     : 2026-05-20
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 혈당 기록 DAO 작성 (2026.05.20 : 배서현)
 */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.birthsafer.db.entity.BloodSugarRecord;
import java.util.List;

@Dao
public interface BloodSugarDao {

    @Insert
    void insert(BloodSugarRecord record);

    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getByDate(int userId, String date);

    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getRecentRecords(int userId, String fromDate);

    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date AND is_fasting = :isFasting")
    List<BloodSugarRecord> getByDateAndType(int userId, String date, boolean isFasting);
}