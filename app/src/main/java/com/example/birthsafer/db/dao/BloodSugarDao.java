package com.example.birthsafer.db.dao;
/*
 * BloodSugarDao
 * Description : 혈당 기록 DAO
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-29
 * Revision History
 *   v1.0.0 - DAO 파일 제작
 *   v1.1.0 - getLatestRecord 메서드 추가 (2026.05.29 : 배서현)
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

    // 특정 날짜의 전체 기록 조회하기
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getByDate(int userId, String date);

    // 최근 7일 기록 (AI 리포트용)
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodSugarRecord> getRecentRecords(int userId, String fromDate);

    // 공복/식후 구분 조회하기
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId AND date = :date AND is_fasting = :isFasting")
    List<BloodSugarRecord> getByDateAndType(int userId, String date, boolean isFasting);

    // 가장 최근 혈당 기록 1개 조회 (메인페이지 용도입니다)
    @Query("SELECT * FROM blood_sugar_records WHERE user_id = :userId ORDER BY recorded_at DESC LIMIT 1")
    BloodSugarRecord getLatestRecord(int userId);

}
