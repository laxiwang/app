package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.WeChatToken;
import com.jhyx.halfroom.constant.WeChatTokenTypeStatus;

public interface WeChatTokenService {
    WeChatToken getWeChatTokenByType(WeChatTokenTypeStatus type);

    Integer updateWeChatToken(WeChatToken weChatToken);

    Integer saveWeChatToken(WeChatToken weChatToken);
}
