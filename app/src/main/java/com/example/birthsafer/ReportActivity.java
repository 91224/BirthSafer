package com.example.birthsafer;
/*
 * report.xml 액티비티
 * Description : 리포트 액티비티
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-04-26
 * Last Update : 2026-05-10
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodPressureDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.report);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);
        String reportType = getIntent().getStringExtra("reportType");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reportXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate fromDate = reportType.equals("weekly")
                ? today.minusDays(6)
                : today.withDayOfMonth(1);
        String fromDateStr = fromDate.toString();

        BloodPressureDao bpDao = AppDatabase.getInstance(this).bloodPressureDao();

        new Thread(() -> {
            List<BloodPressureRecord> bpList = bpDao.getRecentRecords(userId, fromDateStr);

            if (!bpList.isEmpty()) {
                double avgS = 0, avgD = 0;
                for (BloodPressureRecord r : bpList) {
                    avgS += r.systolic;
                    avgD += r.diastolic;
                }
                avgS /= bpList.size();
                avgD /= bpList.size();

                final double finalAvgS = avgS;
                final double finalAvgD = avgD;

                runOnUiThread(() -> {
                    // 평균 수축기 카드 TextView ID: tvAvgSystolic
                    // 평균 이완기 카드 TextView ID: tvAvgDiastolic
                    TextView tvAvgS = findViewById(R.id.tvAvgSystolic);
                    TextView tvAvgD = findViewById(R.id.tvAvgDiastolic);
                    tvAvgS.setText(String.format("%.1f", finalAvgS));
                    tvAvgD.setText(String.format("%.1f", finalAvgD));

                    List<Entry> systolicEntries = new ArrayList<>();
                    List<Entry> diastolicEntries = new ArrayList<>();
                    for (int i = 0; i < bpList.size(); i++) {
                        systolicEntries.add(new Entry(i, bpList.get(i).systolic));
                        diastolicEntries.add(new Entry(i, bpList.get(i).diastolic));
                    }

                    LineDataSet systolicSet = new LineDataSet(systolicEntries, "수축기");
                    systolicSet.setColor(0xFFE57373);
                    systolicSet.setDrawCircles(false);

                    LineDataSet diastolicSet = new LineDataSet(diastolicEntries, "이완기");
                    diastolicSet.setColor(0xFF64B5F6);
                    diastolicSet.setDrawCircles(false);

                    LineChart chart = findViewById(R.id.lineChartBP);
                    chart.setData(new LineData(systolicSet, diastolicSet));
                    chart.getDescription().setEnabled(false);
                    chart.invalidate();
                });
            }

        }).start();

}}
