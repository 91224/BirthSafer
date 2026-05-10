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
 */
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
