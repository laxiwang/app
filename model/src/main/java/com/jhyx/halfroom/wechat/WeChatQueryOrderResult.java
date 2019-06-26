package com.jhyx.halfroom.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WeChatQueryOrderResult {
    private String returnCode;
    private String returnMsg;
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String resultCode;
    private String errCode;
    private String errCodeDes;
    private String openid;
    private String tradeType;
    private String tradeState;
    private String bankType;
    private String totalFee;
    private String cashFee;
    private String transactionId;
    private String outTradeNo;
    private String timeEnd;
    private String tradeStateDesc;
}
