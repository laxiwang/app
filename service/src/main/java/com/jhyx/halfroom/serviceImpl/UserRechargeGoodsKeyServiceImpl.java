package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserRechargeGoodsKey;
import com.jhyx.halfroom.dao.UserRechargeGoodsKeyDao;
import com.jhyx.halfroom.service.UserRechargeGoodsKeyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRechargeGoodsKeyServiceImpl implements UserRechargeGoodsKeyService {
    private UserRechargeGoodsKeyDao userRechargeGoodsKeyDao;

    @Override
    public UserRechargeGoodsKey getUserRechargeGoodsKeyByRechargePoint(Integer rechargePoint) {
        return userRechargeGoodsKeyDao.select_user_recharge_goods_key_by_recharge_point(rechargePoint);
    }
}
