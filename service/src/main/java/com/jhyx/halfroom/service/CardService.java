package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.Card;

public interface CardService {
    Card getCardByCardNo(Integer cardNo);

    Card getCardByCardNoAndCardCodeAndType(Integer cardNo, String cardCode, Integer type);

    Integer updateCard(Card card);
}
