package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.WeChatToken;
import com.jhyx.halfroom.constant.WeChatTokenTypeStatus;
import com.jhyx.halfroom.dao.WeChatTokenDao;
import com.jhyx.halfroom.service.WeChatTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeChatTokenServiceImpl implements WeChatTokenService {
    private WeChatTokenDao weChatTokenDao;

    @Override
    public WeChatToken getWeChatTokenByType(WeChatTokenTypeStatus type) {
        return weChatTokenDao.select_we_chat_token_by_type(type);
    }

    @Override
    public Integer updateWeChatToken(WeChatToken weChatToken) {
        return weChatTokenDao.update_we_chat_token(weChatToken);
    }

    @Override
    public Integer saveWeChatToken(WeChatToken weChatToken) {
        return weChatTokenDao.insert_into_we_chat_token(weChatToken);
    }
}
