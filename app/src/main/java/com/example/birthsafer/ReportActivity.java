package com.example.birthsafer;
/*
 * report.xml 액티비티
 * Description : 리포트 액티비티
 * Author       : 권유진
 * Contributors : 문경민
 * Created     : 2026-04-26
 * Last Update : 2026-05-31
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 *   v1.1.0 - 혈압 트렌드 그래프 및 평균 계산 기능 추가 (2026-05-31 : 문경민)
 */
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
import com.example.birthsafer.db.dao.BloodSugarDao;
import com.example.birthsafer.db.entity.BloodPressureRecord;
import com.example.birthsafer.db.entity.BloodSugarRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.report);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reportXml), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);
        String reportType = getIntent().getStringExtra("reportType");

        LocalDate today = LocalDate.now();
        LocalDate fromDate = "weekly".equals(reportType)
                ? today.minusDays(6)
                : today.withDayOfMonth(1);

        String fromDateStr = fromDate.toString();

        loadBloodPressure(userId, fromDateStr);
        loadBloodSugar(userId, fromDateStr);
    }

    private void loadBloodPressure(int userId, String fromDateStr) {

        BloodPressureDao bpDao =
                AppDatabase.getInstance(this).bloodPressureDao();

        new Thread(() -> {

            List<BloodPressureRecord> bpList =
                    bpDao.getRecentRecords(userId, fromDateStr);

            if (bpList.isEmpty()) return;

            double avgS = 0;
            double avgD = 0;

            for (BloodPressureRecord r : bpList) {
                avgS += r.systolic;
                avgD += r.diastolic;
            }

            avgS /= bpList.size();
            avgD /= bpList.size();

            double finalAvgS = avgS;
            double finalAvgD = avgD;

            runOnUiThread(() -> {

                TextView tvAvgS =
                        findViewById(R.id.tvAvgSystolic);

                TextView tvAvgD =
                        findViewById(R.id.tvAvgDiastolic);

                tvAvgS.setText(String.format("%.1f", finalAvgS));
                tvAvgD.setText(String.format("%.1f", finalAvgD));

                List<Entry> systolicEntries = new ArrayList<>();
                List<Entry> diastolicEntries = new ArrayList<>();

                for (int i = 0; i < bpList.size(); i++) {
                    systolicEntries.add(
                            new Entry(i, bpList.get(i).systolic));

                    diastolicEntries.add(
                            new Entry(i, bpList.get(i).diastolic));
                }

                LineDataSet systolicSet =
                        new LineDataSet(systolicEntries, "수축기");

                systolicSet.setColor(0xFFE57373);
                systolicSet.setDrawCircles(false);

                LineDataSet diastolicSet =
                        new LineDataSet(diastolicEntries, "이완기");

                diastolicSet.setColor(0xFF64B5F6);
                diastolicSet.setDrawCircles(false);

                LineChart chart =
                        findViewById(R.id.lineChartBP);

                chart.setData(
                        new LineData(systolicSet, diastolicSet));

                chart.getDescription().setEnabled(false);
                chart.invalidate();
            });

        }).start();
    }

    private void loadBloodSugar(int userId, String fromDateStr) {

        BloodSugarDao bsDao =
                AppDatabase.getInstance(this).bloodSugarDao();

        new Thread(() -> {

            List<BloodSugarRecord> bsList =
                    bsDao.getRecentRecords(userId, fromDateStr);

            double avgFasting = 0;
            double avgPostmeal = 0;

            int fastingCount = 0;
            int postmealCount = 0;

            List<Entry> fastingEntries = new ArrayList<>();
            List<Entry> postmealEntries = new ArrayList<>();
            List<String> dateLabels = new ArrayList<>();

            for (BloodSugarRecord r : bsList) {

                if (r.isFasting) {

                    avgFasting += r.bloodSugar;

                    fastingEntries.add(
                            new Entry(fastingCount,
                                    r.bloodSugar));

                    fastingCount++;

                } else {

                    avgPostmeal += r.bloodSugar;

                    postmealEntries.add(
                            new Entry(postmealCount,
                                    r.bloodSugar));

                    postmealCount++;
                }

                String label = r.date.substring(5);

                if (dateLabels.isEmpty()
                        || !dateLabels.get(dateLabels.size() - 1)
                        .equals(label)) {

                    dateLabels.add(label);
                }
            }

            if (fastingCount > 0)
                avgFasting /= fastingCount;

            if (postmealCount > 0)
                avgPostmeal /= postmealCount;

            double finalAvgFasting = avgFasting;
            double finalAvgPostmeal = avgPostmeal;

            int finalFastingCount = fastingCount;
            int finalPostmealCount = postmealCount;

            runOnUiThread(() -> {

                TextView tvFBG =
                        findViewById(R.id.tvAvgFBG);

                TextView tvPPG =
                        findViewById(R.id.tvAvgPPG);

                tvFBG.setText(
                        finalFastingCount > 0
                                ? String.format("%.1f", finalAvgFasting)
                                : "-"
                );

                tvPPG.setText(
                        finalPostmealCount > 0
                                ? String.format("%.1f", finalAvgPostmeal)
                                : "-"
                );

                setupBloodSugarChart(
                        fastingEntries,
                        postmealEntries,
                        dateLabels
                );
            });

        }).start();
    }

    private void setupBloodSugarChart(
            List<Entry> fastingEntries,
            List<Entry> postmealEntries,
            List<String> dateLabels) {

        LineChart chart =
                findViewById(R.id.bloodSugarChart);

        LineDataSet fastingSet =
                new LineDataSet(fastingEntries, "공복 혈당");

        fastingSet.setColor(0xFF4A90E2);
        fastingSet.setCircleColor(0xFF4A90E2);
        fastingSet.setLineWidth(2f);
        fastingSet.setCircleRadius(4f);
        fastingSet.setDrawValues(false);
        fastingSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet postmealSet =
                new LineDataSet(postmealEntries, "식후 혈당");

        postmealSet.setColor(0xFFE25C5C);
        postmealSet.setCircleColor(0xFFE25C5C);
        postmealSet.setLineWidth(2f);
        postmealSet.setCircleRadius(4f);
        postmealSet.setDrawValues(false);
        postmealSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        chart.setData(new LineData(fastingSet, postmealSet));

        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(
                new IndexAxisValueFormatter(dateLabels));

        xAxis.setLabelRotationAngle(-45f);
        xAxis.setDrawGridLines(false);

        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);

        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        chart.setExtraBottomOffset(16f);

        chart.animateX(800);
        chart.invalidate();
    }
}