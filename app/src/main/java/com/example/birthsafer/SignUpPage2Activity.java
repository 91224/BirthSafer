package com.example.birthsafer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SignUpPage2Activity extends AppCompatActivity {

    TextInputLayout dueDateLayout;

    TextInputEditText dueDateEditText;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup2_page);

        dueDateLayout =
                findViewById(R.id.tilDueDate);

        dueDateEditText =
                findViewById(R.id.etDueDate);

        nextButton =
                findViewById(R.id.NextButton2);

        dueDateEditText.setOnClickListener(v -> {
            showDatePicker();
        });

        nextButton.setOnClickListener(v -> {
            validateAndNext();
        });
    }

    private void showDatePicker() {

        Calendar calendar =
                Calendar.getInstance();

        int year =
                calendar.get(Calendar.YEAR);

        int month =
                calendar.get(Calendar.MONTH);

        int day =
                calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (view, y, m, d) -> {

                            String date =
                                    y + "-"
                                            + (m + 1)
                                            + "-"
                                            + d;

                            dueDateEditText.setText(date);

                            dueDateLayout.setError(null);

                        },
                        year,
                        month,
                        day
                );

        dialog.show();
    }

    private void validateAndNext() {

        String dueDate =
                dueDateEditText
                        .getText()
                        .toString()
                        .trim();

        if (dueDate.isEmpty()) {

            dueDateLayout.setError(
                    "출산 예정일을 선택하세요"
            );

            return;
        }

        Intent prevIntent = getIntent();

        String name =
                prevIntent.getStringExtra("name");

        String id =
                prevIntent.getStringExtra("id");

        String pw =
                prevIntent.getStringExtra("pw");

        Intent intent =
                new Intent(
                        this,
                        SignUpPage3Activity.class
                );

        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("pw", pw);
        intent.putExtra("dueDate", dueDate);

        startActivity(intent);
    }
}