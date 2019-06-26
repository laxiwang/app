package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserRechargeGoodsKey;
import org.apache.ibatis.annotations.Param;

public interface UserRechargeGoodsKeyDao {
    UserRechargeGoodsKey select_user_recharge_goods_key_by_recharge_point(@Param("rechargePoint") Integer rechargePoint);
}
