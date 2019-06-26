package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserOrder;
import org.apache.ibatis.annotations.Param;

public interface UserOrderDao {
    Integer insert_into_user_order(UserOrder userOrder);

    UserOrder select_user_order_by_order_no(@Param("orderNo") String orderNo);

    Integer update_user_order(UserOrder userOrder);
}
