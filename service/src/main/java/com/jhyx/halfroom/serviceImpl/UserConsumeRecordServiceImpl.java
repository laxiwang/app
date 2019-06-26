package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserConsumeRecord;
import com.jhyx.halfroom.dao.UserConsumeRecordDao;
import com.jhyx.halfroom.service.UserConsumeRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserConsumeRecordServiceImpl implements UserConsumeRecordService {
    private UserConsumeRecordDao userConsumeRecordDao;

    @Override
    public List<UserConsumeRecord> getUserConsumeRecordListByUserId(Long userId){
        return userConsumeRecordDao.select_user_consume_record_by_user_id(userId);
    }

    @Override
    public void saveUserConsumeRecord(UserConsumeRecord userConsumeRecord) {
        userConsumeRecordDao.insert_into_user_consume_record(userConsumeRecord);
    }
}
