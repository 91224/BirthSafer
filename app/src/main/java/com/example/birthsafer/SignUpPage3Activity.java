package com.example.birthsafer;
/*
 * signup3_page.xml 액티비티
 * Description : 날짜별 상세페이지
 * Author       : 김지훈
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
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpPage3Activity extends AppCompatActivity {

    private CheckBox diabetesPreg, hypertensionPreg, diabetes, hypertension;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page_3);

        diabetesPreg = findViewById(R.id.check_diabetes_preg);
        hypertensionPreg = findViewById(R.id.check_hypertension_preg);
        diabetes = findViewById(R.id.check_diabetes);
        hypertension = findViewById(R.id.check_hypertension);

        signUpButton = findViewById(R.id.signInButton);

        signUpButton.setOnClickListener(v -> moveNext());
    }

    private void moveNext() {

        boolean dp = diabetesPreg.isChecked();
        boolean hp = hypertensionPreg.isChecked();
        boolean d = diabetes.isChecked();
        boolean h = hypertension.isChecked();

       //임시가입완료처리(나중에 실제 로직 추가)
        Toast.makeText(this,
                "회원가입 완료 (임시)\nDP:" + dp + " HP:" + hp,
                Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(this, SignInPageActivity.class);
        startActivity(intent);
        finish();
    }
}