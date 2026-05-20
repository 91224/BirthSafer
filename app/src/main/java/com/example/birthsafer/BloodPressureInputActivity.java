/*
 * BloodPressureInputActivity.java
 * Description : 혈압 입력화면 액티비티
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-20
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - Room DB 연동, 혈압 저장 기능 구현 (2026.05.20 : 배서현)
 */

package com.example.birthsafer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BloodPressureInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.blood_pressure_input);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bloodPressureInputXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 현재 시각 표시
        TextView nowTimeTxt = findViewById(R.id.NowTimeTxt);
        String now = new java.text.SimpleDateFormat(
                "yyyy년 MM월 dd일 (E) a hh:mm",
                java.util.Locale.KOREAN
        ).format(new java.util.Date());
        nowTimeTxt.setText(now);

        // XML 입력 요소 연결
        TextInputLayout systolicLayout = findViewById(R.id.BPSystolicInputLayout);
        TextInputLayout diastolicLayout = findViewById(R.id.BPDiastolicInputLayout);

        if (systolicLayout.getEditText() == null || diastolicLayout.getEditText() == null) {
            Toast.makeText(this, "입력창을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputEditText systolicEdit = (TextInputEditText) systolicLayout.getEditText();
        TextInputEditText diastolicEdit = (TextInputEditText) diastolicLayout.getEditText();

        Button saveButton = findViewById(R.id.bloodPressureSaveButton);

        saveButton.setOnClickListener(v -> {
            String sStr = systolicEdit.getText() == null
                    ? ""
                    : systolicEdit.getText().toString().trim();

            String dStr = diastolicEdit.getText() == null
                    ? ""
                    : diastolicEdit.getText().toString().trim();

            if (sStr.isEmpty()) {
                systolicLayout.setError("수축기 혈압을 입력하세요");
                return;
            } else {
                systolicLayout.setError(null);
            }

            if (dStr.isEmpty()) {
                diastolicLayout.setError("이완기 혈압을 입력하세요");
                return;
            } else {
                diastolicLayout.setError(null);
            }

            int systolic;
            int diastolic;

            try {
                systolic = Integer.parseInt(sStr);
                diastolic = Integer.parseInt(dStr);
            } catch (NumberFormatException e) {
                systolicLayout.setError("올바른 숫자를 입력하세요");
                return;
            }

            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int userId = prefs.getInt("logged_in_user_id", -1);

            String dateTime = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.getDefault()
            ).format(new java.util.Date());

            String date = dateTime.substring(0, 10);

            BloodPressureRecord record = new BloodPressureRecord();
            record.userId = userId;
            record.systolic = systolic;
            record.diastolic = diastolic;
            record.recordedAt = dateTime;
            record.date = date;

            BloodPressureDao dao = AppDatabase.getInstance(this).bloodPressureDao();

            new Thread(() -> {
                dao.insert(record);

                runOnUiThread(() -> {
                    Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}