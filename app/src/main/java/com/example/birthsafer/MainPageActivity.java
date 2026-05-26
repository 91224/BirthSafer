package com.example.birthsafer;
/*
 * main_page.xml 액티비티
 * Description : 메인페이지
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
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

// 🔥 추가
import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

        // 유저 정보 불러오기
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        TextView userNameTxt = findViewById(R.id.txt1);
        TextView dueDateTxt = findViewById(R.id.dueDateTxt);
        TextView dDayTxt = findViewById(R.id.dDayTxt);

        new Thread(() -> {
            User user = AppDatabase.getInstance(this).userDao().findById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    userNameTxt.setText("안녕하세요, " + user.name + "님");
                    dueDateTxt.setText(user.dueDate);
                    LocalDate today = LocalDate.now();
                    LocalDate dueDate = LocalDate.parse(user.dueDate,
                            DateTimeFormatter.ofPattern("yyyy-M-d")); // DB 저장 형식 맞춤
                    long dDay = ChronoUnit.DAYS.between(today, dueDate);

                    if (dDay > 0) {
                        dDayTxt.setText("D-" + dDay);
                    } else if (dDay == 0) {
                        dDayTxt.setText("D-Day");
                    } else {
                        dDayTxt.setText("D+" + Math.abs(dDay));
                    }
                }
            });
        }).start();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_main_page) {
                return true;
            }

            else if (item.getItemId() == R.id.nav_summary_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarDetailActivity.class);
                startActivity(intent);
                return true;
            }

            else if (item.getItemId() == R.id.nav_calendar_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            }

            else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(MainPageActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}