package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.CardUsedRecord;

import java.util.List;

public interface CardUsedRecordService {
    List<CardUsedRecord> getCardUsedRecordByUserIdAndCardType(Long userId, Integer cardType);

    void saveCardUsedRecord(CardUsedRecord cardUsedRecord);
}
