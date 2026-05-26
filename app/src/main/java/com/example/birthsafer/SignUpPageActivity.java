/*
 * signup_page.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 문경민
 * Contributors : 권유진, 허원
 * Created     : 2026-04-26
 * Last Update : 2026-05-26
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 액티비티 파일 연결 (2026-05-04 : 권유진)
 *   v1.2.0 - 이름/아이디/비밀번호 입력 및 ID 중복 확인 기능 구현 (2026-05-22 : 허원)
 *   v1.2.1 - 비밀번호 해시 추가 (2026-05-26 : 권유진)
 */
package com.example.birthsafer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.UserDao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpPageActivity extends AppCompatActivity {

    TextInputLayout nameLayout;
    TextInputLayout idLayout;
    TextInputLayout pwLayout;

    TextInputEditText nameEditText;
    TextInputEditText idEditText;
    TextInputEditText pwEditText;

    Button nextButton;

    UserDao userDao;

    boolean isDuplicateChecked = false;

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

        userDao = AppDatabase
                .getInstance(this)
                .userDao();

        nextButton.setOnClickListener(v -> {
            validateAndNext();
        });
    }

    private void validateAndNext() {

        String name =
                nameEditText.getText().toString().trim();

        String id =
                idEditText.getText().toString().trim();

        String pw =
                pwEditText.getText().toString().trim();

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

        new Thread(() -> {

            if (userDao.findByUsername(id) != null) {

                runOnUiThread(() -> {
                    idLayout.setError("이미 사용중인 아이디입니다");
                });

            } else {

                runOnUiThread(() -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    SignUpPage2Activity.class
                            );

                    intent.putExtra("name", name);
                    intent.putExtra("id", id);
                    intent.putExtra("pw",HashUtils.sha256(pw));//해시

                    startActivity(intent);
                });
            }

        }).start();
    }
}