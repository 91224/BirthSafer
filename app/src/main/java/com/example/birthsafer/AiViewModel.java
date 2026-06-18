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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class AiViewModel extends ViewModel{
    private final MutableLiveData<String> aiComment = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<String> getAiComment() { return aiComment; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void analyzeHealth(int bs, int bp, int Pregnanceweek) {
        isLoading.setValue(true);

        AiComment.generateComment(bs, bp, Pregnanceweek,
                new AiComment.AiCallBack() {
                    @Override
                    public void Success(String comment) {
                        aiComment.postValue(comment);
                        isLoading.postValue(false);
                    }

                    @Override
                    public void Error(String error) {
                        aiComment.postValue(error);
                        isLoading.postValue(false);
                    }
                });
    }
}
