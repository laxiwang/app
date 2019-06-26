package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.IntegralExchangeRecord;
import com.jhyx.halfroom.bean.IntegralExchangeRule;
import com.jhyx.halfroom.bean.IntegralOrigin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IntegrationDao {
    List<IntegralOrigin> select_integral_origin_by_user_id(@Param("userId") Long userId);

    Integer select_integral_exchange_record_by_book_id(@Param("bookId") Integer bookId);

    List<IntegralExchangeRule> select_integral_exchange_rule();

    List<IntegralExchangeRecord> select_integral_exchange_record_by_user_id(@Param("userId") Long userId);

    IntegralExchangeRule select_integral_exchange_rule_by_book_id(@Param("bookId") Integer bookId);

    Integer insert_into_integral_exchange_record(IntegralExchangeRecord exchange);

    void insert_into_integral_origin(IntegralOrigin integration);
}
