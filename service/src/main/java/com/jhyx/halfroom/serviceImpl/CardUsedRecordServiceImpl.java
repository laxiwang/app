package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.CardUsedRecord;
import com.jhyx.halfroom.dao.CardUsedRecordDao;
import com.jhyx.halfroom.service.CardUsedRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CardUsedRecordServiceImpl implements CardUsedRecordService {
    private CardUsedRecordDao cardUsedRecordDao;

    @Override
    public List<CardUsedRecord> getCardUsedRecordByUserIdAndCardType(Long userId, Integer cardType) {
        return cardUsedRecordDao.select_card_used_record_by_user_id_and_card_type(userId, cardType);
    }

    @Override
    public void saveCardUsedRecord(CardUsedRecord cardUsedRecord) {
        cardUsedRecordDao.insert_into_card_used_record(cardUsedRecord);
    }
}
