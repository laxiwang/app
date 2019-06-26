package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.ActivityUserPrize;
import org.apache.ibatis.annotations.Param;

public interface ActivityUserPrizeDao {
    ActivityUserPrize select_activity_user_prize_by_user_id(@Param("userId") Long userId);

    void insert_into_activity_user_prize(ActivityUserPrize userPrize);
}
