package com.example.birthsafer;
/*
 * signup2_page.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 허원
 * Contributors : 권유진
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 액티비티 파일 연결 (2026-05-04 : 권유진)
 */
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SignUpPage2Activity extends AppCompatActivity {

    private TextInputLayout dueDateLayout;
    private TextInputEditText dueDateEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page_2);

        dueDateLayout = findViewById(R.id.tilDueDate);
        dueDateEditText = findViewById(R.id.etDueDate);
        nextButton = findViewById(R.id.signInButton);

        dueDateEditText.setOnClickListener(v -> showDatePicker());

        nextButton.setOnClickListener(v -> validateAndNext());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    String date = y + "-" + (m + 1) + "-" + d;
                    dueDateEditText.setText(date);
                    dueDateLayout.setError(null);
                },
                year, month, day
        );

        dialog.show();
    }

    private void validateAndNext() {
        String dueDate = dueDateEditText.getText().toString().trim();

        if (dueDate.isEmpty()) {
            dueDateLayout.setError("출산 예정일을 선택하세요");
            return;
        }

        String name = getIntent().getStringExtra("name");
        String id = getIntent().getStringExtra("id");
        String pw = getIntent().getStringExtra("pw");


        Intent intent = new Intent(this, SignUpPage3Activity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("pw", pw);
        intent.putExtra("dueDate", dueDate);
        startActivity(intent);
    }
}