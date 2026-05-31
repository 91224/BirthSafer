package com.example.birthsafer;
/*
 * calendar_detail.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-29
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 사용자 이름/예정일/D-Day/기저질환 표시, 기저질환 JSON 파싱 (2026.05.29 : 배서현)
 */
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.widget.TextView;
import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MyPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mypage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myPageXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 사용자 정보 불러오기
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        TextView userNameTxt = findViewById(R.id.userNameTxt);
        TextView myPageDueDateTxt = findViewById(R.id.myPageDueDateTxt);
        TextView dDayText = findViewById(R.id.dDayText);
        TextView conditionTxt = findViewById(R.id.conditionTxt);

        new Thread(() -> {
            User user = AppDatabase.getInstance(this).userDao().findById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    userNameTxt.setText(user.name);
                    myPageDueDateTxt.setText(user.dueDate);

                    // D-Day 계산
                    LocalDate today = LocalDate.now();
                    LocalDate dueDate = LocalDate.parse(user.dueDate,
                            DateTimeFormatter.ofPattern("yyyy-M-d"));
                    long dDay = ChronoUnit.DAYS.between(today, dueDate);
                    if (dDay > 0) {
                        dDayText.setText("D-" + dDay);
                    } else if (dDay == 0) {
                        dDayText.setText("D-Day");
                    } else {
                        dDayText.setText("D+" + Math.abs(dDay));
                    }

                    // 기저질환 (JSON 파싱)
                    try {
                        org.json.JSONObject flags = new org.json.JSONObject(user.diseaseFlags);
                        java.util.List<String> diseases = new java.util.ArrayList<>();
                        if (flags.optBoolean("gestDiabetes"))     diseases.add("임신성 당뇨");
                        if (flags.optBoolean("gestHypertension")) diseases.add("임신성 고혈압");
                        if (flags.optBoolean("baseDiabetes"))     diseases.add("당뇨");
                        if (flags.optBoolean("baseHypertension")) diseases.add("고혈압");
                        if (diseases.isEmpty()) {
                            conditionTxt.setText("없음");
                        } else {
                            conditionTxt.setText(String.join(", ", diseases));
                        }
                    } catch (Exception e) {
                        conditionTxt.setText("없음");
                    }
                }
            });
        }).start();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_main_page) {
                startActivity(new Intent(this, MainPageActivity.class));
                return true;
            }

            else if (id == R.id.nav_summary_page) {
                startActivity(new Intent(this, CalendarDetailActivity.class));
                return true;
            }

            else if (id == R.id.nav_calendar_page) {
                startActivity(new Intent(this, CalendarActivity.class));
                return true;
            }

            else if (id == R.id.nav_mypage) {
                // 현재 화면
                return true;
            }

            return false;
        });
    }}