package com.example.birthsafer;

/*
 * MainPageActivity.java
 * Description : 메인페이지
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 인사 문구, 오늘 혈압/혈당 기록 조회 추가 (2026.05.20 : 배서현)
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPageXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SharedPreferences에서 유저 정보 읽기
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String username = prefs.getString("logged_in_username", "사용자");
        int userId = prefs.getInt("logged_in_user_id", -1);

        // 인사 문구 표시
        TextView txt1 = findViewById(R.id.txt1);
        txt1.setText("안녕하세요, " + username + "님");

        // 오늘 날짜
        String today = new java.text.SimpleDateFormat(
                "yyyy-MM-dd", java.util.Locale.getDefault()
        ).format(new java.util.Date());

        // 오늘 기록 조회
        BloodPressureDao bpDao = AppDatabase.getInstance(this).bloodPressureDao();
        BloodSugarDao bsDao = AppDatabase.getInstance(this).bloodSugarDao();

        new Thread(() -> {
            java.util.List<BloodPressureRecord> bpList = bpDao.getByDate(userId, today);
            java.util.List<BloodSugarRecord> bsList = bsDao.getByDate(userId, today);
            runOnUiThread(() -> {
                if (!bpList.isEmpty()) {
                    BloodPressureRecord latest = bpList.get(bpList.size() - 1);
                    ((TextView) findViewById(R.id.bloodPressureValueTxt))
                            .setText(latest.systolic + "/" + latest.diastolic);
                }
                if (!bsList.isEmpty()) {
                    BloodSugarRecord latest = bsList.get(bsList.size() - 1);
                    ((TextView) findViewById(R.id.bloodSugarValueTxt))
                            .setText(String.valueOf(latest.bloodSugar));
                }
            });
        }).start();

        Button BPInputbutton = findViewById(R.id.bloodPressureInputButton);
        Button BSInputbutton = findViewById(R.id.bloodSugarInputButton);
        Button StInputbutton = findViewById(R.id.symptomInputButton);

        BPInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, BloodPressureInputActivity.class);
            startActivity(intent);
        });

        BSInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, BloodSugarInputActivity.class);
            startActivity(intent);
        });

        StInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, SymptomInputActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_main_page) {
                return true;
            } else if (item.getItemId() == R.id.nav_summary_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarDetailActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_calendar_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(MainPageActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}