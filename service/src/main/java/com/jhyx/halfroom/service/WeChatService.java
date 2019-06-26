package com.jhyx.halfroom.service;

import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderParams;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface WeChatService {
    Map<String, String> signAPPPay(WeChatPayUnifiedOrderParams params) throws Exception;

    Map<String, String> signWapPay(WeChatPayUnifiedOrderParams params) throws Exception;

    Boolean queryOrderPayStatus(@NotNull String orderNo);

    Map<String, String> wapShareSign(String url);

    String getWapOpenId(@NotNull String code);
}
