/*
 * blood_Sugar_input.xml 액티비티
 * Description : 혈당 입력화면 액티비티
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 */
package com.example.birthsafer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BloodSugarInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.blood_sugar_input);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bloodSugarInputXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 현재 시각 표시
        TextView nowTimeTxt = findViewById(R.id.NowTimeTxt);
        nowTimeTxt.setText(new java.text.SimpleDateFormat(
                "yyyy년 MM월 dd일 (E) a hh:mm",
                java.util.Locale.KOREAN
        ).format(new java.util.Date()));

        TextInputLayout sugarLayout = findViewById(R.id.BSSugarInputLayout);

        if (sugarLayout.getEditText() == null) {
            Toast.makeText(this, "입력창을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        TextInputEditText sugarEdit = (TextInputEditText) sugarLayout.getEditText();
        CheckBox checkFBG = findViewById(R.id.checkBoxFBG);
        CheckBox checkPPG = findViewById(R.id.checkBoxPPG);
        Button saveButton = findViewById(R.id.bloodSugarSaveButton);

        // 공복/식후 하나만 선택
        checkFBG.setOnCheckedChangeListener((btn, checked) -> {
            if (checked) checkPPG.setChecked(false);
        });
        checkPPG.setOnCheckedChangeListener((btn, checked) -> {
            if (checked) checkFBG.setChecked(false);
        });

        saveButton.setOnClickListener(v -> {
            String sStr = sugarEdit.getText() == null
                    ? ""
                    : sugarEdit.getText().toString().trim();

            if (sStr.isEmpty()) {
                sugarLayout.setError("혈당 수치를 입력하세요");
                return;
            } else {
                sugarLayout.setError(null);
            }

            if (!checkFBG.isChecked() && !checkPPG.isChecked()) {
                Toast.makeText(this, "공복 또는 식후를 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            int bloodSugar;
            try {
                bloodSugar = Integer.parseInt(sStr);
            } catch (NumberFormatException e) {
                sugarLayout.setError("올바른 숫자를 입력하세요");
                return;
            }

            boolean isFasting = checkFBG.isChecked();
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int userId = prefs.getInt("logged_in_user_id", -1);

            String dateTime = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.getDefault()
            ).format(new java.util.Date());

            BloodSugarRecord record = new BloodSugarRecord();
            record.userId     = userId;
            record.bloodSugar = bloodSugar;
            record.isFasting  = isFasting;
            record.recordedAt = dateTime;
            record.date       = dateTime.substring(0, 10);

            BloodSugarDao dao = AppDatabase.getInstance(this).bloodSugarDao();

            new Thread(() -> {
                dao.insert(record);
                runOnUiThread(() -> {
                    // ALT-002: 이상 수치 알림 (알림 기능 구현 후 연결)
                    // if (isFasting && bloodSugar >= 126) { ... }
                    // if (!isFasting && bloodSugar >= 200) { ... }
                    Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}