package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserDao {
    User select_user_by_phone(@Param("phone") String phone);

    Integer insert_into_user(User user);

    User select_user_by_id(@Param("id") Long id);

    Integer update_user(User user);

    List<User> select_user_by_ids(@Param("ids") Collection<Long> ids);

    Long select_user_count();

    Long select_user_count_by_register_introducer(@Param("ambassadorId") Long ambassadorId);
}
