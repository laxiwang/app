package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserAnswerInfo;

import java.util.List;

public interface UserAnswerInfoService {
    String userAnswerInfo(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    List<UserAnswerInfo> userAnswerInfoList(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    void updateUserAnswerInfo(UserAnswerInfo userAnswerInfo);

    void saveUserAnswerInfo(UserAnswerInfo userAnswerInfo);
}
