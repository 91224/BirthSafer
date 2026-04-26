package com.example.birthsafer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 로그인 화면 레이아웃을 화면에 표시합니다.
        setContentView(R.layout.activity_login);
    }
}