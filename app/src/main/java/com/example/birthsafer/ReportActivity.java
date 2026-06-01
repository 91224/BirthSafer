package com.example.birthsafer;
/*
 * report.xml 액티비티
 * Description : 리포트 액티비티
 * Author       : 권유진
 * Contributors : 김지훈
 * Created     : 2026-04-26
 * Last Update : 2026-06-01
 * Revision History
 *   v1.0.0 - 액티비티 파일 제작
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birthsafer.db.AppDatabase;
import com.example.birthsafer.db.dao.BloodSugarDao;
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
        setContentView(R.layout.report);

        // userId, reportType 수신
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);
        String reportType = getIntent().getStringExtra("reportType"); // "weekly" or "monthly"

        // 팀원 A 방식과 통일한 날짜 범위 계산
        LocalDate today    = LocalDate.now();
        LocalDate fromDate = "weekly".equals(reportType)
                ? today.minusDays(6)          // 주간: 오늘 포함 7일
                : today.withDayOfMonth(1);    // 월간: 이번 달 1일부터
        String fromDateStr = fromDate.toString(); // yyyy-MM-dd

        // ── 혈당 조회 및 카드/그래프 업데이트 ──────────────────────────
        BloodSugarDao bsDao = AppDatabase.getInstance(this).bloodSugarDao();

        new Thread(() -> {
            List<BloodSugarRecord> bsList = bsDao.getRecentRecords(userId, fromDateStr);

            // 평균 계산
            double avgFasting = 0, avgPostmeal = 0;
            int fastingCount = 0, postmealCount = 0;

            // 그래프 데이터
            List<Entry>  fastingEntries  = new ArrayList<>();
            List<Entry>  postmealEntries = new ArrayList<>();
            List<String> dateLabels      = new ArrayList<>();

            for (BloodSugarRecord r : bsList) {
                if (r.isFasting) {
                    avgFasting += r.bloodSugar;
                    fastingEntries.add(new Entry(fastingCount, r.bloodSugar));
                    fastingCount++;
                } else {
                    avgPostmeal += r.bloodSugar;
                    postmealEntries.add(new Entry(postmealCount, r.bloodSugar));
                    postmealCount++;
                }
                // X축 날짜 레이블 (중복 제거, MM-dd 형식)
                String label = r.date.substring(5);
                if (dateLabels.isEmpty() || !dateLabels.get(dateLabels.size() - 1).equals(label)) {
                    dateLabels.add(label);
                }
            }

            if (fastingCount  > 0) avgFasting  /= fastingCount;
            if (postmealCount > 0) avgPostmeal /= postmealCount;

            final double finalFasting      = avgFasting;
            final double finalPostmeal     = avgPostmeal;
            final int    finalFastingCount  = fastingCount;
            final int    finalPostmealCount = postmealCount;
            final List<Entry>  finalFastingEntries  = fastingEntries;
            final List<Entry>  finalPostmealEntries = postmealEntries;
            final List<String> finalDateLabels      = dateLabels;

            runOnUiThread(() -> {
                // 평균 혈당 카드 업데이트
                TextView tvFBG = findViewById(R.id.tvAvgFBG);
                TextView tvPPG = findViewById(R.id.tvAvgPPG);
                tvFBG.setText(finalFastingCount  > 0 ? String.format("%.1f", finalFasting)  : "-");
                tvPPG.setText(finalPostmealCount > 0 ? String.format("%.1f", finalPostmeal) : "-");

                // 혈당 트렌드 그래프
                setupBloodSugarChart(finalFastingEntries, finalPostmealEntries, finalDateLabels);
            });
        }).start();
    }

    // ── 혈당 LineChart 세팅 ────────────────────────────────────────────
    private void setupBloodSugarChart(
            List<Entry> fastingEntries,
            List<Entry> postmealEntries,
            List<String> dateLabels) {

        LineChart chart = findViewById(R.id.bloodSugarChart);

        // 공복 혈당 라인 (파란색)
        LineDataSet fastingSet = new LineDataSet(fastingEntries, "공복 혈당");
        fastingSet.setColor(0xFF4A90E2);
        fastingSet.setCircleColor(0xFF4A90E2);
        fastingSet.setLineWidth(2f);
        fastingSet.setCircleRadius(4f);
        fastingSet.setDrawValues(false);
        fastingSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // 식후 혈당 라인 (빨간색)
        LineDataSet postmealSet = new LineDataSet(postmealEntries, "식후 혈당");
        postmealSet.setColor(0xFFE25C5C);
        postmealSet.setCircleColor(0xFFE25C5C);
        postmealSet.setLineWidth(2f);
        postmealSet.setCircleRadius(4f);
        postmealSet.setDrawValues(false);
        postmealSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // 차트 데이터 세팅
        chart.setData(new LineData(fastingSet, postmealSet));

        // X축 설정
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setDrawGridLines(false);

        // 차트 전체 설정
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.setExtraBottomOffset(16f); // X축 레이블 잘림 방지
        chart.animateX(800);
        chart.invalidate();
    }
}