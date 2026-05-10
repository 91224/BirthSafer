package com.example.birthsafer;
/*
 * signin_page.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 배서현
 * Contributors : 권유진
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 액티비티 파일 연결 (2026-05-04 : 권유진)
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInPageActivity extends AppCompatActivity {

    private TextInputLayout idLayout;
    private TextInputLayout pwLayout;
    private TextInputEditText idEditText;
    private TextInputEditText pwEditText;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin_page);

        idLayout = findViewById(R.id.idInputTextInputLayout);
        pwLayout = findViewById(R.id.pwInputTextInputLayout);

        idEditText = (TextInputEditText) idLayout.getEditText();
        pwEditText = (TextInputEditText) pwLayout.getEditText();

        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(v -> login());
        signUpButton.setOnClickListener(v -> moveToSignUp());
    }

    private void login() {
        String id = idEditText.getText().toString().trim();
        String pw = pwEditText.getText().toString().trim();

        boolean isValid = true;

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

        Toast.makeText(this, "로그인 성공 (임시)", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToSignUp() {
        Intent intent = new Intent(this, SignUpPageActivity.class);
        startActivity(intent);
    }
}