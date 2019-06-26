package com.jhyx.halfroom.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WeChatPayUnifiedOrderResult {
    private String returnCode;

    private String returnMsg;

    private String appId;

    private String mchId;

    private String nonceStr;

    private String sign;

    private String resultCode;

    private String errorCode;

    private String errorCodeDesc;

    private String prepayId;

    private String tradeType;
}
