package com.example.birthsafer;
/*
 * calendar_detail.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.0.1 - 무한 실행 버그 해결
 */
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.example.birthsafer.db.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import android.content.SharedPreferences;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;

public class CalendarDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.calendar_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calendarDetailXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        BloodPressureDao bpDao = AppDatabase.getInstance(this).bloodPressureDao();
        BloodSugarDao bsDao = AppDatabase.getInstance(this).bloodSugarDao();


        // AI 식단 추천
        new Thread(() -> {
            BloodPressureRecord latestBP = bpDao.getLatestRecord(userId);
            BloodSugarRecord latestBS = bsDao.getLatestRecord(userId);
            User user = AppDatabase.getInstance(this).userDao().findById(userId);

            if (latestBP == null || latestBS == null || user == null) return;

            LocalDate today2 = LocalDate.now();
            LocalDate dueDate = LocalDate.parse(user.dueDate,
                    DateTimeFormatter.ofPattern("yyyy-M-d"));
            long daysLeft = ChronoUnit.DAYS.between(today2, dueDate);
            int pregnancyWeek = (int) ((280 - daysLeft) / 7);

            AiComment.generateComment(
                    latestBS.bloodSugar,
                    latestBP.systolic,
                    pregnancyWeek,
                    new AiComment.AiCallBack() {
                        @Override
                        public void Success(String comment) {
                            runOnUiThread(() -> {
                                TextView aiDietTxt = findViewById(R.id.aiDietTxt2);
                                aiDietTxt.setText(comment);
                            });
                        }

                        @Override
                        public void Error(String error) {
                            runOnUiThread(() -> {
                                TextView aiDietTxt = findViewById(R.id.aiDietTxt2);
                                aiDietTxt.setText("AI 추천을 불러올 수 없습니다.");
                            });
                        }
                    }
            );
        }).start();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_main_page) {
                startActivity(new Intent(this, MainPageActivity.class));
                return true;
            }

            else if (item.getItemId() == R.id.nav_summary_page) {
                return true; // 현재 화면이므로 그냥 true만
            }

            else if (item.getItemId() == R.id.nav_calendar_page) {
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            }

            else if (item.getItemId() == R.id.nav_mypage) {
                startActivity(new Intent(this, MyPageActivity.class));
                return true;
            }

            return false;
        });
    }
}
