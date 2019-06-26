package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.IntegralExchangeRecord;
import com.jhyx.halfroom.bean.IntegralExchangeRule;
import com.jhyx.halfroom.bean.IntegralOrigin;

import java.util.List;

public interface IntegralService {
    Integer getIntegralGoodsExchangeCount(Integer bookId);

    List<IntegralExchangeRule> getIntegralExchangeRuleList();

    Integer getUserIntegral(Long userId);

    IntegralExchangeRule getIntegralExchangeRuleByBookId(Integer bookId);

    Integer saveIntegralExchange(IntegralExchangeRecord exchange);

    List<IntegralExchangeRecord> getIntegralExchangeListByUserId(Long userId);

    List<IntegralOrigin> getIntegralListByUserId(Long userId);

    void saveIntegral(IntegralOrigin integration);
}
