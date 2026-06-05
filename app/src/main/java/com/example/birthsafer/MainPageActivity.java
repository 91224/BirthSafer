package com.example.birthsafer;
/*
 * main_page.xml 액티비티
 * Description : 메인페이지
 * Author       : 권유진
 * Contributors : 배서현
 * Created     : 2026-04-26
 * Last Update : 2026-05-29
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 혈압/혈당/증상 입력값 메인 카드에 표시 (2026.05.29 : 배서현)
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// 🔥 추가
import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.entity.User;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.dao.SymptomDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.example.birthsafer.db.entity.SymptomRecord;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainPageXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button BPInputbutton = findViewById(R.id.bloodPressureInputButton);
        Button BSInputbutton = findViewById(R.id.bloodSugarInputButton);
        Button StInputbutton = findViewById(R.id.symptomInputButton);

        BPInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, BloodPressureInputActivity.class);
            startActivity(intent);
        });

        BSInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, BloodSugarInputActivity.class);
            startActivity(intent);
        });

        StInputbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, SymptomInputActivity.class);
            startActivity(intent);
        });

        // 유저 정보 불러오기
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        TextView userNameTxt = findViewById(R.id.txt1);
        TextView dueDateTxt = findViewById(R.id.dueDateTxt);
        TextView dDayTxt = findViewById(R.id.dDayTxt);

        new Thread(() -> {
            User user = AppDatabase.getInstance(this).userDao().findById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    userNameTxt.setText("안녕하세요, " + user.name + "님");
                    dueDateTxt.setText(user.dueDate);
                    LocalDate today = LocalDate.now();
                    LocalDate dueDate = LocalDate.parse(user.dueDate,
                            DateTimeFormatter.ofPattern("yyyy-M-d")); // DB 저장 형식 맞춤
                    long dDay = ChronoUnit.DAYS.between(today, dueDate);

                    if (dDay > 0) {
                        dDayTxt.setText("D-" + dDay);
                    } else if (dDay == 0) {
                        dDayTxt.setText("D-Day");
                    } else {
                        dDayTxt.setText("D+" + Math.abs(dDay));
                    }
                }
            });
        }).start();

        // 혈압/혈당/증상 TextView 연결
        TextView bloodPressureValueTxt = findViewById(R.id.bloodPressureValueTxt);
        TextView systolicRangeTxt = findViewById(R.id.systolicRangeTxt);
        TextView diastolicRangeTxt = findViewById(R.id.diastolicRangeTxt);
        TextView bloodSugarValueTxt = findViewById(R.id.bloodSugarValueTxt);
        TextView bloodSugarRangeTxt = findViewById(R.id.bloodSugarRangeTxt);
        TextView symptomSummaryTxt = findViewById(R.id.symptomSummaryTxt);

        String today = new java.text.SimpleDateFormat(
                "yyyy-MM-dd", java.util.Locale.getDefault()
        ).format(new java.util.Date());

        BloodPressureDao bpDao = AppDatabase.getInstance(this).bloodPressureDao();
        BloodSugarDao bsDao = AppDatabase.getInstance(this).bloodSugarDao();
        SymptomDao symptomDao = AppDatabase.getInstance(this).symptomDao();

        // 혈압 카드
        new Thread(() -> {
            BloodPressureRecord latest = bpDao.getLatestRecord(userId);
            List<BloodPressureRecord> todayList = bpDao.getByDate(userId, today);
            runOnUiThread(() -> {
                if (latest != null) {
                    bloodPressureValueTxt.setText(latest.systolic + "/" + latest.diastolic);
                } else {
                    bloodPressureValueTxt.setText("?/?");
                }
                if (todayList.isEmpty()) {
                    systolicRangeTxt.setText("아직 혈압을 입력하지 않았어요");
                    diastolicRangeTxt.setText("");
                } else if (todayList.size() == 1) {
                    systolicRangeTxt.setText("수축기 " + todayList.get(0).systolic);
                    diastolicRangeTxt.setText("이완기 " + todayList.get(0).diastolic);
                } else {
                    int minS = todayList.stream().mapToInt(r -> r.systolic).min().getAsInt();
                    int maxS = todayList.stream().mapToInt(r -> r.systolic).max().getAsInt();
                    int minD = todayList.stream().mapToInt(r -> r.diastolic).min().getAsInt();
                    int maxD = todayList.stream().mapToInt(r -> r.diastolic).max().getAsInt();
                    systolicRangeTxt.setText("수축기 " + minS + "-" + maxS);
                    diastolicRangeTxt.setText("이완기 " + minD + "-" + maxD);
                }
            });
        }).start();

        // 혈당 카드
        new Thread(() -> {
            BloodSugarRecord latest = bsDao.getLatestRecord(userId);
            List<BloodSugarRecord> todayList = bsDao.getByDate(userId, today);
            runOnUiThread(() -> {
                if (latest != null) {
                    bloodSugarValueTxt.setText(String.valueOf(latest.bloodSugar));
                } else {
                    bloodSugarValueTxt.setText("?");
                }
                if (todayList.isEmpty()) {
                    bloodSugarRangeTxt.setText("아직 혈당을 입력하지 않았어요");
                } else if (todayList.size() == 1) {
                    bloodSugarRangeTxt.setText(String.valueOf(todayList.get(0).bloodSugar));
                } else {
                    int min = todayList.stream().mapToInt(r -> r.bloodSugar).min().getAsInt();
                    int max = todayList.stream().mapToInt(r -> r.bloodSugar).max().getAsInt();
                    bloodSugarRangeTxt.setText(min + "-" + max);
                }
            });
        }).start();

        // 증상 카드
        new Thread(() -> {
            List<SymptomRecord> todaySymptoms = symptomDao.getByDate(userId, today);
            runOnUiThread(() -> {
                if (todaySymptoms.isEmpty()) {
                    symptomSummaryTxt.setText("아직 증상을 입력하지 않았어요");
                } else {
                    List<String> names = new java.util.ArrayList<>();
                    SymptomRecord s = todaySymptoms.get(0);
                    if (s.nausea > 0)    names.add("메스꺼움");
                    if (s.fatigue > 0)   names.add("피로감");
                    if (s.headache > 0)  names.add("두통");
                    if (s.edema > 0)     names.add("부종");
                    if (s.dizziness > 0) names.add("어지러움");
                    if (names.isEmpty()) {
                        symptomSummaryTxt.setText("아직 증상을 입력하지 않았어요");
                    } else {
                        symptomSummaryTxt.setText(String.join(", ", names));
                    }
                }
            });
        }).start();

        // AI 식단 추천
        new Thread(() -> {
            BloodPressureRecord latestBP = bpDao.getLatestRecord(userId);
            BloodSugarRecord latestBS = bsDao.getLatestRecord(userId);
            User user = AppDatabase.getInstance(this).userDao().findById(userId);

            if (latestBP == null || latestBS == null || user == null) return;

            LocalDate today2 = LocalDate.now();
            LocalDate dueDate = LocalDate.parse(user.dueDate,
                    DateTimeFormatter.ofPattern("yyyy-M-d"));
            long daysLeft = ChronoUnit.DAYS.between(today2, dueDate);
            int pregnancyWeek = (int) ((280 - daysLeft) / 7);

            AiComment.generateComment(
                    latestBS.bloodSugar,
                    latestBP.systolic,
                    pregnancyWeek,
                    new AiComment.AiCallBack() {
                        @Override
                        public void Success(String comment) {
                            runOnUiThread(() -> {
                                TextView aiDietTxt = findViewById(R.id.aiDietTxt);
                                aiDietTxt.setText(comment);
                            });
                        }

                        @Override
                        public void Error(String error) {
                            runOnUiThread(() -> {
                                TextView aiDietTxt = findViewById(R.id.aiDietTxt);
                                aiDietTxt.setText("AI 추천을 불러올 수 없습니다.");
                            });
                        }
                    }
            );
        }).start();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_main_page) {
                return true;
            }

            else if (item.getItemId() == R.id.nav_summary_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarDetailActivity.class);
                startActivity(intent);
                return true;
            }

            else if (item.getItemId() == R.id.nav_calendar_page) {
                Intent intent = new Intent(MainPageActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            }

            else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(MainPageActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}