package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserRechargeRecord;
import com.jhyx.halfroom.dao.UserRechargeRecordDao;
import com.jhyx.halfroom.service.UserRechargeRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRechargeRecordServiceImpl implements UserRechargeRecordService {
    private UserRechargeRecordDao userRechargeRecordDao;

    @Override
    public void saveUserRechargeRecord(UserRechargeRecord rechargeRecord) {
        userRechargeRecordDao.insert_into_user_recharge_record(rechargeRecord);
    }

    @Override
    public UserRechargeRecord getUserRechargeRecordById(String id) {
        return userRechargeRecordDao.select_user_recharge_record_by_id(id);
    }

    @Override
    public void updateUserRechargeRecord(UserRechargeRecord rechargeRecord) {
        userRechargeRecordDao.update_user_recharge_record(rechargeRecord);
    }

    @Override
    public List<UserRechargeRecord> getUserRechargeRecordListByUserId(Long userId) {
        return userRechargeRecordDao.select_user_recharge_record_by_user_id(userId);
    }
}
