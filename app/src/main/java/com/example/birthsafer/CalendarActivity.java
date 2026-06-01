package com.example.birthsafer;
/*
 * calendar.xml 액티비티
 * Description : 캘린더 화면 액티비티
 * Author       : 권유진
 * Contributors : 허원
 * Created     : 2026-04-26
 * Last Update : 2026-05-29
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 컬러 인디케이터 구현 (2026.05.29 : 허원)
 *   v1.2.0 - 주간 리포트 버튼,월간 리포트 버튼 추가 (26.06.01 : 지훈)
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        calendarView = findViewById(R.id.calendarView);



        YearMonth currentMonth = YearMonth.now();
        calendarView.setup(
                currentMonth.minusMonths(12),
                currentMonth.plusMonths(12),
                DayOfWeek.SUNDAY
        );
        calendarView.scrollToMonth(currentMonth);


        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @Override
            public DayViewContainer create(View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(DayViewContainer container, CalendarDay day) {
                container.textView.setText(
                        String.valueOf(day.getDate().getDayOfMonth())
                );

                if (day.getPosition() == DayPosition.MonthDate) {
                    container.textView.setVisibility(View.VISIBLE);
                } else {
                    container.textView.setVisibility(View.INVISIBLE);
                }

                if (day.getDate().equals(LocalDate.now())) {
                    container.textView.setBackgroundResource(R.drawable.bg_today);
                } else {
                    container.textView.setBackground(null);
                }
                SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                int userId = prefs.getInt("logged_in_user_id", -1);

                String dateStr = day.getDate().toString();

                BloodPressureDao bpDao =
                        AppDatabase.getInstance(CalendarActivity.this).bloodPressureDao();

                BloodSugarDao bsDao =
                        AppDatabase.getInstance(CalendarActivity.this).bloodSugarDao();

                new Thread(() -> {

                    List<BloodPressureRecord> bpList =
                            bpDao.getByDate(userId, dateStr);

                    List<BloodSugarRecord> bsList =
                            bsDao.getByDate(userId, dateStr);

                    int level = 0;
                    boolean hasHypoglycemia = false;

                    for (BloodPressureRecord r : bpList) {

                        int l;

                        if (r.systolic >= 160 || r.diastolic >= 110)
                            l = 4;
                        else if (r.systolic >= 140 || r.diastolic >= 90)
                            l = 3;
                        else if (r.systolic >= 120 || r.diastolic >= 80)
                            l = 2;
                        else
                            l = 1;

                        if (l > level)
                            level = l;
                    }

                    for (BloodSugarRecord r : bsList) {

                        int l;

                        if (r.bloodSugar < 60) {
                            hasHypoglycemia = true;
                            l = 5;
                        }
                        else if (r.isFasting) {

                            if (r.bloodSugar >= 95)
                                l = 4;
                            else if (r.bloodSugar >= 90)
                                l = 2;
                            else
                                l = 1;
                        }
                        else {

                            if (r.bloodSugar >= 120)
                                l = 4;
                            else if (r.bloodSugar >= 110)
                                l = 2;
                            else
                                l = 1;
                        }

                        if (l > level)
                            level = l;
                    }

                    final int finalLevel = level;
                    final boolean finalHypo = hasHypoglycemia;

                    runOnUiThread(() -> {

                        if (finalLevel == 0) {

                            container.indicator.setVisibility(View.GONE);
                            return;
                        }

                        container.indicator.setVisibility(View.VISIBLE);

                        int color;

                        if (finalHypo) {
                            color = android.graphics.Color.parseColor("#9C27B0");
                        }
                        else if (finalLevel >= 4) {
                            color = android.graphics.Color.parseColor("#F44336");
                        }
                        else if (finalLevel == 3) {
                            color = android.graphics.Color.parseColor("#FF9800");
                        }
                        else if (finalLevel == 2) {
                            color = android.graphics.Color.parseColor("#FFC107");
                        }
                        else {
                            color = android.graphics.Color.parseColor("#4CAF50");
                        }

                        container.indicator.setBackgroundTintList(
                                ColorStateList.valueOf(color)
                        );
                    });

                }).start();

                container.getView().setOnClickListener(v -> {
                    Toast.makeText(CalendarActivity.this,
                            day.getDate().toString(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
            @Override
            public MonthHeaderContainer create(View view) {
                return new MonthHeaderContainer(view);
            }

            @Override
            public void bind(MonthHeaderContainer container, CalendarMonth month) {
                container.yearText.setText(String.valueOf(month.getYearMonth().getYear()));
                container.monthTitle.setText(month.getYearMonth().getMonthValue() + "월");
            }
        });
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_calendar_page) {
                return true;
            }

            else if (id == R.id.nav_main_page) {
                startActivity(new Intent(this, MainPageActivity.class));
                return true;
            }

            else if (id == R.id.nav_summary_page) {
                startActivity(new Intent(this, CalendarDetailActivity.class));
                return true;
            }

            else if (id == R.id.nav_mypage) {
                startActivity(new Intent(this, MyPageActivity.class));
                return true;
            }

            return false;
        });

        // 주간 리포트 버튼
        Button report1Button = findViewById(R.id.report1Button);
        report1Button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("reportType", "weekly");
            startActivity(intent);
        });

        // 월간 리포트 버튼
        Button report2Button = findViewById(R.id.report2Button);
        report2Button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("reportType", "monthly");
            startActivity(intent);
        });

    }

    class DayViewContainer extends ViewContainer {
        TextView textView;
        View indicator;

        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.dayText);
            indicator = view.findViewById(R.id.indicator);
        }
    }

    class MonthHeaderContainer extends ViewContainer {
        TextView monthTitle;
        TextView yearText;
        public MonthHeaderContainer(View view) {
            super(view);
            monthTitle = view.findViewById(R.id.monthYearText);
            yearText = view.findViewById(R.id.yearText);
        }
    }
}