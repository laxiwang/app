package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserOrder;

public interface UserOrderService {
    Integer saveUserOrder(UserOrder userOrder);

    UserOrder getUserOrderByOrderNo(String orderNo);

    Integer updateUserOrder(UserOrder userOrder);
}
