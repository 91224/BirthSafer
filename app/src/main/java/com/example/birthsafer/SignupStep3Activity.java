package com.example.birthsafer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupStep3Activity extends AppCompatActivity {

    CheckBox checkDiabetesPreg, checkHypertensionPreg, checkDiabetes, checkHypertension;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CheckBox 연결
        checkDiabetesPreg = findViewById(R.id.check_diabetes_preg);
        checkHypertensionPreg = findViewById(R.id.check_hypertension_preg);
        checkDiabetes = findViewById(R.id.check_diabetes);
        checkHypertension = findViewById(R.id.check_hypertension);

        // Button 연결
        btnSignup = findViewById(R.id.btn_signup);

        // 버튼 클릭 이벤트
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = "선택된 질환:\n";

                if (checkDiabetesPreg.isChecked()) {
                    result += "임신성 당뇨\n";
                }

                if (checkHypertensionPreg.isChecked()) {
                    result += "임신성 고혈압\n";
                }

                if (checkDiabetes.isChecked()) {
                    result += "당뇨 (기저질환)\n";
                }

                if (checkHypertension.isChecked()) {
                    result += "고혈압 (기저질환)\n";
                }

                if (result.equals("선택된 질환:\n")) {
                    result = "선택된 질환이 없습니다.";
                }


                //  로그인 화면으로 이동
                Intent intent = new Intent(SignupStep3Activity.this, MainActivity.class);
                startActivity(intent);

                // 현재 화면 종료 (뒤로가기 방지)
                finish();
            }
        });
    }
}