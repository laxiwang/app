package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserConsumeRecord;

import java.util.List;

public interface UserConsumeRecordService {
    List<UserConsumeRecord> getUserConsumeRecordListByUserId(Long userId);

    void saveUserConsumeRecord(UserConsumeRecord userConsumeRecord);
}
