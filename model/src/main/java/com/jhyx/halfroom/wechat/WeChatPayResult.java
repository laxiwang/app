package com.jhyx.halfroom.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeChatPayResult {
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
	private String isSubscribe;
	private String tradeType;
	private String bankType;
	private int totalFee;
	private int cashFee;
	private String transactionId;
	private String outTradeNo;
	private String timeEnd;
}
