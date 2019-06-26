package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Card;
import com.jhyx.halfroom.dao.CardDao;
import com.jhyx.halfroom.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {
    private CardDao cardDao;

    @Override
    public Card getCardByCardNoAndCardCodeAndType(Integer cardNo, String cardCode, Integer type) {
        return cardDao.select_card_by_card_no_and_card_code_and_type(cardNo, cardCode, type);
    }

    @Override
    public Card getCardByCardNo(Integer cardNo) {
        return cardDao.select_card_by_card_no(cardNo);
    }

    @Override
    public Integer updateCard(Card card) {
        return cardDao.update_card(card);
    }
}
