package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserConsumeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserConsumeRecordDao {
    List<UserConsumeRecord> select_user_consume_record_by_user_id(@Param("userId") Long userId);

    void insert_into_user_consume_record(UserConsumeRecord userConsumeRecord);
}
