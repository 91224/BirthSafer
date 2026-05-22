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
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// 🔥 추가
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