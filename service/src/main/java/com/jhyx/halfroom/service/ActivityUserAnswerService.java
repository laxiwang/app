package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.ActivityUserAnswer;

import java.util.Collection;
import java.util.List;

public interface ActivityUserAnswerService {
    ActivityUserAnswer getActivityUserAnswerByUserId(Long userId);

    Collection<ActivityUserAnswer> getActivityUserAnswerByUserIds(Collection<Long> userIds);

    void saveActivityUserAnswer(ActivityUserAnswer activityUserAnswer);

    void updateActivityUserAnswer(ActivityUserAnswer activityUserAnswer);

    List<ActivityUserAnswer> getAllActivityUserAnswer();
}
