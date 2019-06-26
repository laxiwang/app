package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.Card;
import org.apache.ibatis.annotations.Param;

public interface CardDao {
    Card select_card_by_card_no(@Param("cardNo") Integer cardNo);

    Card select_card_by_card_no_and_card_code_and_type(@Param("cardNo") Integer cardNo,
                                                       @Param("cardCode") String cardCode,
                                                       @Param("type") Integer type);

    Integer update_card(Card card);
}
