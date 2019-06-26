package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.IntegralExchangeRecord;
import com.jhyx.halfroom.bean.IntegralExchangeRule;
import com.jhyx.halfroom.bean.IntegralOrigin;
import com.jhyx.halfroom.dao.IntegrationDao;
import com.jhyx.halfroom.service.IntegralService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IntegrationServiceImpl implements IntegralService {
    private IntegrationDao integrationDao;

    @Override
    public Integer getIntegralGoodsExchangeCount(Integer bookId) {
        return integrationDao.select_integral_exchange_record_by_book_id(bookId);
    }

    @Override
    public List<IntegralExchangeRule> getIntegralExchangeRuleList() {
        return integrationDao.select_integral_exchange_rule();
    }

    @Override
    public Integer getUserIntegral(Long userId) {
        int integral = 0;
        if (userId != null) {
            List<IntegralOrigin> list = integrationDao.select_integral_origin_by_user_id(userId);
            int acquired = list.stream().map(IntegralOrigin::getIntegral).mapToInt(Integer::intValue).sum();
            List<IntegralExchangeRecord> exchanges = integrationDao.select_integral_exchange_record_by_user_id(userId);
            int consumed = exchanges.stream().map(IntegralExchangeRecord::getIntegral).mapToInt(Integer::intValue).sum();
            integral = acquired - consumed;
        }
        return integral > 0 ? integral : 0;
    }

    @Override
    public IntegralExchangeRule getIntegralExchangeRuleByBookId(Integer bookId) {
        return integrationDao.select_integral_exchange_rule_by_book_id(bookId);
    }

    @Override
    public Integer saveIntegralExchange(IntegralExchangeRecord exchange) {
        return integrationDao.insert_into_integral_exchange_record(exchange);
    }

    @Override
    public List<IntegralExchangeRecord> getIntegralExchangeListByUserId(Long userId) {
        return integrationDao.select_integral_exchange_record_by_user_id(userId);
    }

    @Override
    public List<IntegralOrigin> getIntegralListByUserId(Long userId) {
        return integrationDao.select_integral_origin_by_user_id(userId);
    }

    @Override
    public void saveIntegral(IntegralOrigin integration) {
        integrationDao.insert_into_integral_origin(integration);
    }
}
