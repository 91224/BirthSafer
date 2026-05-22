/*
 * SignUpPage3Activity
 * Description : 회원가입 3단계 화면
 * Author : 허원
 * Contributors : 권유진
 *
 * Created : 2026-05-18
 * Last Update : 2026-05-22
 *
 * Revision History
 * - 질환 정보 입력 및 Room DB 회원가입 저장 기능 구현
 */
package com.example.birthsafer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.UserDao;
import com.example.birthsafer.db.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpPage3Activity extends AppCompatActivity {

    CheckBox diabetesPreg;
    CheckBox hypertensionPreg;
    CheckBox diabetes;
    CheckBox hypertension;

    Button signUpButton;

    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_page_3);

        diabetesPreg =
                findViewById(R.id.check_diabetes_preg);

        hypertensionPreg =
                findViewById(R.id.check_hypertension_preg);

        diabetes =
                findViewById(R.id.check_diabetes);

        hypertension =
                findViewById(R.id.check_hypertension);

        signUpButton =
                findViewById(R.id.NextButton3);

        userDao =
                AppDatabase
                        .getInstance(this)
                        .userDao();

        signUpButton.setOnClickListener(v -> {
            moveNext();
        });
    }

    private void moveNext() {

        boolean dp =
                diabetesPreg.isChecked();

        boolean hp =
                hypertensionPreg.isChecked();

        boolean d =
                diabetes.isChecked();

        boolean h =
                hypertension.isChecked();

        // Intent에서 이전 단계 데이터 수신
        String name =
                getIntent().getStringExtra("name");

        String id =
                getIntent().getStringExtra("id");

        String pw =
                getIntent().getStringExtra("pw");

        String dueDate =
                getIntent().getStringExtra("dueDate");

        // diseaseFlags JSON 생성
        String diseaseFlags =
                "{\"gestDiabetes\":" + dp
                        + ",\"gestHypertension\":" + hp
                        + ",\"baseDiabetes\":" + d
                        + ",\"baseHypertension\":" + h
                        + "}";

        // User 객체 생성
        User user = new User();

        user.username = id;
        user.password = pw;
        user.name = name;
        user.dueDate = dueDate;
        user.diseaseFlags = diseaseFlags;

        user.notifyHour = 20;
        user.notifyMinute = 0;

        user.createdAt =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                ).format(new Date());

        new Thread(() -> {

            userDao.insertUser(user);

            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        "회원가입이 완료되었습니다",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent =
                        new Intent(
                                this,
                                SignInPageActivity.class
                        );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                );

                startActivity(intent);

            });

        }).start();
    }
}