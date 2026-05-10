package com.example.birthsafer;
/*
 * blood_pressure_input.xml 액티비티
 * Description : 혈압 입력화면 액티비티
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BloodPressureInputActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.blood_pressure_input);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bloodPressureInputXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Button idbutton = findViewById(R.id.idInputTextInputLayout);
            Button pwbutton = findViewById(R.id.pwInputTextInputLayout);

            idbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BloodPressureInputActivity.this, MainPageActivity.class);
                    startActivity(intent);
                }
            });

            idbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BloodPressureInputActivity.this, MainPageActivity.class);
                    startActivity(intent);
                }
            });
            return insets;
        });

    }


}
