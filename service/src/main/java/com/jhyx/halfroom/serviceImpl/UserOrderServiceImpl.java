package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserOrder;
import com.jhyx.halfroom.dao.UserOrderDao;
import com.jhyx.halfroom.service.UserOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {
    private UserOrderDao userOrderDao;

    @Override
    public Integer saveUserOrder(UserOrder userOrder) {
        return userOrderDao.insert_into_user_order(userOrder);
    }

    @Override
    public UserOrder getUserOrderByOrderNo(String orderNo) {
        return userOrderDao.select_user_order_by_order_no(orderNo);
    }

    @Override
    public Integer updateUserOrder(UserOrder userOrder) {
        return userOrderDao.update_user_order(userOrder);
    }
}
