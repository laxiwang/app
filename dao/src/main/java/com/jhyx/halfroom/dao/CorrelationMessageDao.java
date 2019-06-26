package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.CorrelationMessage;
import org.apache.ibatis.annotations.Param;

public interface CorrelationMessageDao {
    Integer insert_into_correlation_message(CorrelationMessage correlationMessage);

    CorrelationMessage select_correlation_message_by_correlation_id(@Param("correlationId") String correlationId);

    Integer update_correlation_message(CorrelationMessage correlationMessage);
}
