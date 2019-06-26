package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.CardUsedRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardUsedRecordDao {
    void insert_into_card_used_record(CardUsedRecord cardUsedRecord);

    List<CardUsedRecord> select_card_used_record_by_user_id_and_card_type(@Param("userId") Long userId,
                                                                          @Param("cardType") Integer cardType);
}
