package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.ActivityUserAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ActivityUserAnswerDao {
    ActivityUserAnswer select_activity_user_answer_by_user_id(@Param("userId") Long userId);

    void insert_into_activity_user_answer(ActivityUserAnswer activityUserAnswer);

    void update_activity_user_answer(ActivityUserAnswer activityUserAnswer);

    List<ActivityUserAnswer> select_activity_user_answer();

    Collection<ActivityUserAnswer> select_activity_user_answer_by_user_ids(@Param("userIds") Collection<Long> userIds);
}
