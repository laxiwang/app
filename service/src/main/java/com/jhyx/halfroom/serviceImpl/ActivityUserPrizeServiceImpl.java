package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.ActivityUserPrize;
import com.jhyx.halfroom.dao.ActivityUserPrizeDao;
import com.jhyx.halfroom.service.ActivityUserPrizeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityUserPrizeServiceImpl implements ActivityUserPrizeService {
    private ActivityUserPrizeDao activityUserPrizeDao;

    @Override
    public ActivityUserPrize getUserPrizeByUserId(Long userId) {
        return activityUserPrizeDao.select_activity_user_prize_by_user_id(userId);
    }

    @Override
    public void saveUserPrize(ActivityUserPrize userPrize) {
        activityUserPrizeDao.insert_into_activity_user_prize(userPrize);
    }
}