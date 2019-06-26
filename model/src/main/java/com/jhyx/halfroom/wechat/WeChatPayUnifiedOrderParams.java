package com.jhyx.halfroom.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WeChatPayUnifiedOrderParams {
    private String appId;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String body;
    private String outTradeNo;
    private Integer totalFee;
    private String spbillCreateIp;
    private String notifyUrl;
    private String tradeType;
    private String openId;
}
