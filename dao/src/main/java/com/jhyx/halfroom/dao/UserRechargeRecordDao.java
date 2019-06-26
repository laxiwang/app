package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserRechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRechargeRecordDao {
    void insert_into_user_recharge_record(UserRechargeRecord rechargeRecord);

    UserRechargeRecord select_user_recharge_record_by_id(@Param("id") String id);

    void update_user_recharge_record(UserRechargeRecord rechargeRecord);

    List<UserRechargeRecord> select_user_recharge_record_by_user_id(@Param("userId") Long userId);
}
