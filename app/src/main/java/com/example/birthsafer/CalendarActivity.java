package com.example.birthsafer;
/*
 * calendar.xml 액티비티
 * Description : 캘린더 화면 액티비티
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

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
    }

    class DayViewContainer extends ViewContainer {
        TextView textView;
        public DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.dayText);
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