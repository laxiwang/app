package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.Manual;
import com.jhyx.halfroom.bean.UserManual;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserManualDao {
    List<UserManual> select_user_manual_by_user_id(@Param("userId") Long userId);

    void batch_insert_into_user_manual(@Param("list") Collection<UserManual> userManualList);

    List<Manual> select_user_manual_and_manual_by_user_id(@Param("userId") Long userId);
}
