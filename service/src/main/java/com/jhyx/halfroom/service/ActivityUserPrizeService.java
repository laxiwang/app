package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.ActivityUserPrize;

public interface ActivityUserPrizeService {
    ActivityUserPrize getUserPrizeByUserId(Long userId);

    void saveUserPrize(ActivityUserPrize userPrize);
}
