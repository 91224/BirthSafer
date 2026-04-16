package com.example.birthsafer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SignupStep2Activity extends AppCompatActivity {

    EditText etDueDate;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signup2);

        etDueDate = findViewById(R.id.etDueDate);
        btnNext = findViewById(R.id.btnNext);

        // 날짜 입력칸 클릭하면 달력 팝업
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // 다음 단계 버튼
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDueDate.getText().toString();
                if (date.isEmpty()) {
                    Toast.makeText(SignupStep2Activity.this,
                            "출산 예정일을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 다음 화면으로 이동 (나중에 팀이랑 연결)
                Toast.makeText(SignupStep2Activity.this,
                        "선택된 날짜: " + date, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = year + "." + String.format("%02d", month + 1)
                    + "." + String.format("%02d", day);
            etDueDate.setText(date);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}