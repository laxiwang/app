package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserRechargeRecord;

import java.util.List;

public interface UserRechargeRecordService {
    void saveUserRechargeRecord(UserRechargeRecord rechargeRecord);

    UserRechargeRecord getUserRechargeRecordById(String id);

    void updateUserRechargeRecord(UserRechargeRecord rechargeRecord);

    List<UserRechargeRecord> getUserRechargeRecordListByUserId(Long userId);
}
