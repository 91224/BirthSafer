package com.example.birthsafer.db;

/*
 * AppDatabase.java
 * Description : 앱 전체 Room DB를 관리하는 클래스
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-05-20
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 혈압/혈당 Room DB 연결 구조 생성 (2026.05.20 : 배서현)
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;

@Database(
        entities = {
                BloodPressureRecord.class,
                BloodSugarRecord.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BloodPressureDao bloodPressureDao();
    public abstract BloodSugarDao bloodSugarDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "birthsafer_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}