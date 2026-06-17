package com.example.birthsafer.db.dao;
/*
 * BloodPressureDao
 * Description : 혈압 기록 DAO
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

import com.example.birthsafer.db.entity.BloodPressureRecord;
import java.util.List;

@Dao
public interface BloodPressureDao {

    @Insert
    void insert(BloodPressureRecord record);

    // 특정 날짜의 기록 전체 조회하기
    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date = :date ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getByDate(int userId, String date);

    // 최근 7일의 기록 조회하기 (AI 리포트 용도입니다)
    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId AND date >= :fromDate ORDER BY recorded_at ASC")
    List<BloodPressureRecord> getRecentRecords(int userId, String fromDate);

    // 특정 날짜의 최대값 (캘린더 인디케이터 용도입니다)
    @Query("SELECT MAX(systolic) FROM blood_pressure_records WHERE user_id = :userId AND date = :date")
    Integer getMaxSystolicByDate(int userId, String date);

    // 가장 최근 혈압 기록 1개 조회 (메인페이지 용도입니다)
    @Query("SELECT * FROM blood_pressure_records WHERE user_id = :userId ORDER BY recorded_at DESC LIMIT 1")
    BloodPressureRecord getLatestRecord(int userId);
}