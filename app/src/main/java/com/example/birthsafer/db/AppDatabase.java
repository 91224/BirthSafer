package com.example.birthsafer.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.dao.SymptomDao;
import com.example.birthsafer.db.dao.UserDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.example.birthsafer.db.entity.SymptomRecord;
import com.example.birthsafer.db.entity.User;

@Database(
        entities = {User.class, BloodPressureRecord.class, BloodSugarRecord.class, SymptomRecord.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract BloodPressureDao bloodPressureDao();
    public abstract BloodSugarDao bloodSugarDao();
    public abstract SymptomDao symptomDao();

    private static volatile AppDatabase INSTANCE;
    // 앱 전체에서 DB 인스턴스를 하나만 사용 (싱글톤)
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "birthsafer_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public static synchronized void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}