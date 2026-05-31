package com.example.birthsafer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String username;        // 로그인 아이디 (중복 불가)

    @ColumnInfo(name = "password")
    public String password;        // SHA-256 해시값

    @ColumnInfo(name = "name")
    public String name;            // 사용자 실명

    @ColumnInfo(name = "due_date")
    public String dueDate;         // 출산 예정일 (yyyy-M-d)

    @ColumnInfo(name = "disease_flags")
    public String diseaseFlags;    // JSON 문자열
    // {"gestDiabetes":true,"gestHypertension":false,
    //  "baseDiabetes":false,"baseHypertension":false}

    @ColumnInfo(name = "notify_hour")
    public int notifyHour;         // 알림 시각 - 시 (기본값 20)

    @ColumnInfo(name = "notify_minute")
    public int notifyMinute;       // 알림 시각 - 분 (기본값 0)

    @ColumnInfo(name = "created_at")
    public String createdAt;       // 계정 생성일시 (yyyy-MM-dd HH:mm:ss)
}
