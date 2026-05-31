/*
 * symptom_input.xml 액티비티
 * Description : 증상 입력화면
 * Author       : 권유진
 * Contributors : 김지훈, 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-21
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - Room Db 연결 (2026.05.21 : 김지훈)
 *   v1.2.0 - 현재 날짜 및 시간 표시 형식 수정 (2026.05.29 : 배서현)
 */
package com.example.birthsafer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.SymptomDao;
import com.example.birthsafer.db.entity.SymptomRecord;
import com.google.android.material.slider.Slider;

public class SymptomInputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.symptom_input);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.symptomInputXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView nowTimeTxt = findViewById(R.id.NowTimeTxt);
        nowTimeTxt.setText(new java.text.SimpleDateFormat(
                "M월 d일 (E) a h:mm", java.util.Locale.KOREAN
        ).format(new java.util.Date()));

        Slider sliderNausea    = findViewById(R.id.slider);
        Slider sliderFatigue   = findViewById(R.id.slider2);
        Slider sliderHeadache  = findViewById(R.id.slider3);
        Slider sliderEdema     = findViewById(R.id.slider4);
        Slider sliderDizziness = findViewById(R.id.slider5);
        Button saveButton = findViewById(R.id.symptomSaveButton);

        saveButton.setOnClickListener(v -> {
            int nausea    = (int) sliderNausea.getValue();
            int fatigue   = (int) sliderFatigue.getValue();
            int headache  = (int) sliderHeadache.getValue();
            int edema     = (int) sliderEdema.getValue();
            int dizziness = (int) sliderDizziness.getValue();

            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int userId = prefs.getInt("logged_in_user_id", -1);

            String dateTime = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()
            ).format(new java.util.Date());

            SymptomRecord record = new SymptomRecord();
            record.userId     = userId;
            record.nausea     = nausea;
            record.fatigue    = fatigue;
            record.headache   = headache;
            record.edema      = edema;
            record.dizziness  = dizziness;
            record.recordedAt = dateTime;
            record.date       = dateTime.substring(0, 10);

            SymptomDao dao = AppDatabase.getInstance(this).symptomDao();

            new Thread(() -> {
                dao.insert(record);
                runOnUiThread(() -> {
                    // ALT-003: 심각도 4 이상이 2개 이상이면 알림 (알림 기능 구현 후 연결)
                    int severeCount = 0;
                    if (nausea >= 4) severeCount++;
                    if (fatigue >= 4) severeCount++;
                    if (headache >= 4) severeCount++;
                    if (edema >= 4) severeCount++;
                    if (dizziness >= 4) severeCount++;
                    if (severeCount >= 2) {
                        Toast.makeText(
                                this,
                                "위험 증상이 감지되었습니다.",
                                Toast.LENGTH_LONG
                        ).show();}
                    Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SymptomInputActivity.this, MainPageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                });
            }).start();

        });
    }
}