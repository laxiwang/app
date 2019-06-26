package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.ActivityUserAnswer;
import com.jhyx.halfroom.dao.ActivityUserAnswerDao;
import com.jhyx.halfroom.service.ActivityUserAnswerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ActivityUserAnswerServiceImpl implements ActivityUserAnswerService {
    private ActivityUserAnswerDao activityUserAnswerDao;

    @Override
    public ActivityUserAnswer getActivityUserAnswerByUserId(Long userId) {
        return activityUserAnswerDao.select_activity_user_answer_by_user_id(userId);
    }

    @Override
    public Collection<ActivityUserAnswer> getActivityUserAnswerByUserIds(Collection<Long> userIds) {
        return activityUserAnswerDao.select_activity_user_answer_by_user_ids(userIds);
    }

    @Override
    public List<ActivityUserAnswer> getAllActivityUserAnswer() {
        return activityUserAnswerDao.select_activity_user_answer();
    }

    @Override
    public void saveActivityUserAnswer(ActivityUserAnswer activityUserAnswer) {
        activityUserAnswerDao.insert_into_activity_user_answer(activityUserAnswer);
    }

    @Override
    public void updateActivityUserAnswer(ActivityUserAnswer activityUserAnswer) {
        activityUserAnswerDao.update_activity_user_answer(activityUserAnswer);
    }
}
