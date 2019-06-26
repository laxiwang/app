package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.WeChatToken;
import com.jhyx.halfroom.constant.WeChatTokenTypeStatus;
import org.apache.ibatis.annotations.Param;

public interface WeChatTokenDao {
    WeChatToken select_we_chat_token_by_type(@Param("type") WeChatTokenTypeStatus type);

    Integer update_we_chat_token(WeChatToken weChatToken);

    Integer insert_into_we_chat_token(WeChatToken weChatToken);
}
