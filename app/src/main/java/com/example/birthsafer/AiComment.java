/*
 * AIComment
 * Description : AI 프롬포트 및 피드백 생성하는 코드입니다
 * Author       : 권유진
 * Contributors :
 * Created     : 2026-05-31
 * Last Update : 2026-06-05
 * Revision History
 *   v1.0.0 - 파일 생성
 *   v1.1.0 - AI 버전 변경 (2.0->2.5 flash) (2026.06.05 : 권유진)
 */

package com.example.birthsafer;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import java.util.concurrent.Executors;

public class AiComment {
    public interface AiCallBack{
        void Success(String comment);
        void Error(String error);
    }

    public static void generateComment(int bs, int bp, int Pregnanceweek, AiCallBack callBack) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String apiKey = BuildConfig.GEMINI_API_KEY;
                String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

                String prompt = "임산부 건강 데이터를 분석해서 권장하는 영양소와 활동을 추천해줘.\n" +
                "- 임신 주차: " + Pregnanceweek + "주\n" +
                "- 혈당: " + bs + " mg/dL\n" +
                "- 혈압: " + bp + " mmHg\n\n" +
                "규칙:\n" +
                "1. 2문장 이내로 짧게\n" +
                "2. 전문적이지만 따뜻한 말투\n" +
                "3. 수치가 정상이면 칭찬, 이상이면 부드럽게 주의 권고\n" +
                "4. 병원 진단 대신 참고용임을 암시";

                JSONObject part = new JSONObject();
                part.put("text", prompt);

                JSONArray parts = new JSONArray();
                parts.put(part);

                JSONObject content = new JSONObject();
                content.put("parts", parts);

                JSONArray contents = new JSONArray();
                contents.put(content);

                JSONObject body = new JSONObject();
                body.put("contents", contents);

                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(body.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                android.util.Log.e("AiComment", "응답코드: " + responseCode); // ← 추가

                java.io.InputStream is = responseCode == 200
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String response = sb.toString();
                android.util.Log.e("AiComment", "응답내용: " + response); // ← 추가

                if (responseCode == 200) {
                    JSONObject json = new JSONObject(response);
                    String result = json
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    new Handler(Looper.getMainLooper()).post(() -> callBack.Success(result));
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> callBack.Error("AI 추천을 불러올 수 없습니다."));
                }

            } catch (Exception e) {
                android.util.Log.e("AiComment", "오류 상세: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callBack.Error("AI 추천을 불러올 수 없습니다."));
            }
        });
    }
}