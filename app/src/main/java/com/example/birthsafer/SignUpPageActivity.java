package com.example.birthsafer;
/*
 * signup_page.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 문경민
 * Contributors : 권유진
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 액티비티 파일 연결 (2026-05-04 : 권유진)
 */
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpPageActivity extends AppCompatActivity {

    private TextInputLayout nameLayout, idLayout, pwLayout;
    private TextInputEditText nameEditText, idEditText, pwEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        nameLayout = findViewById(R.id.signUpNameLayout);
        idLayout = findViewById(R.id.signUpIdLayout);
        pwLayout = findViewById(R.id.signUpPwLayout);

        nameEditText = findViewById(R.id.signUpNameEditText);
        idEditText = findViewById(R.id.signUpIdEditText);
        pwEditText = findViewById(R.id.signUpPwEditText);

        nextButton = findViewById(R.id.NextButton);

        nextButton.setOnClickListener(v -> validateAndNext());
    }

    private void validateAndNext() {
        String name = nameEditText.getText().toString().trim();
        String id = idEditText.getText().toString().trim();
        String pw = pwEditText.getText().toString().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            nameLayout.setError("이름을 입력하세요");
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (id.isEmpty()) {
            idLayout.setError("아이디를 입력하세요");
            isValid = false;
        } else {
            idLayout.setError(null);
        }

        if (pw.isEmpty()) {
            pwLayout.setError("비밀번호를 입력하세요");
            isValid = false;
        } else {
            pwLayout.setError(null);
        }

        if (!isValid) return;

            Intent intent = new Intent(this, SignUpPage2Activity.class);
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.putExtra("pw", pw);
            startActivity(intent);
    }
}
