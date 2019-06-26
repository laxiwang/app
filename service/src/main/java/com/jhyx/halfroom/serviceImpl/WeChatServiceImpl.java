package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.WeChatToken;
import com.jhyx.halfroom.commons.*;
import com.jhyx.halfroom.constant.WeChatTokenTypeStatus;
import com.jhyx.halfroom.service.WeChatService;
import com.jhyx.halfroom.service.WeChatTokenService;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderParams;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderResult;
import com.jhyx.halfroom.wechat.WeChatQueryOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.app.appId}")
    private String appAppId;
    @Value("${wechat.app.mchId}")
    private String appMchId;
    @Value("${wechat.app.key}")
    private String appKey;
    @Value("${wechat.wap.key}")
    private String wapKey;
    @Value("${wechat.unifiedOrderUrl}")
    private String unifiedOrderUrl;
    @Value("${wechat.queryOrderUrl}")
    private String queryOrderUrl;
    @Value("${wechat.wap.appId}")
    private String wapAppId;
    @Value("${wechat.wap.appSecret}")
    private String wapAppSecret;
    @Value("${wechat.wap.accessTokenUrl}")
    private String wapAccessTokenUrl;
    @Value("${wechat.wap.ticketUrl}")
    private String wapTicketUrl;
    @Value("${wechat.wap.authUrl}")
    private String authUrl;
    private final WeChatTokenService weChatTokenService;

    @Override
    public Map<String, String> signAPPPay(WeChatPayUnifiedOrderParams params) throws Exception {
        Map<String, String> result = new HashMap<>();
        WeChatPayUnifiedOrderResult preOrderResult = unifiedOrder(params, appKey);
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        result.put("appid", appAppId);
        result.put("partnerid", appMchId);
        result.put("timestamp", timeStamp);
        result.put("noncestr", preOrderResult.getNonceStr());
        result.put("prepayid", preOrderResult.getPrepayId());
        result.put("package", "Sign=WXPay");
        result.put("sign", WeChatPayUtils.genPackageSign(result, appKey));
        result.put("outTradeNo", params.getOutTradeNo());
        return result;
    }

    @Override
    public Map<String, String> signWapPay(WeChatPayUnifiedOrderParams params) throws Exception {
        Map<String, String> result = new HashMap<>();
        WeChatPayUnifiedOrderResult preOrderResult = unifiedOrder(params, wapKey);
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        result.put("appId", wapAppId);
        result.put("timeStamp", timeStamp);
        result.put("nonceStr", preOrderResult.getNonceStr());
        result.put("package", "prepay_id=" + preOrderResult.getPrepayId());
        result.put("signType", "MD5");
        result.put("paySign", WeChatPayUtils.genPackageSign(result, wapKey));
        return result;
    }

    @Override
    public Boolean queryOrderPayStatus(@NotNull String orderNo) {
        final String SUCCESS = "SUCCESS";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appAppId);
        params.put("mch_id", appMchId);
        params.put("out_trade_no", orderNo);
        params.put("nonce_str", UniqueKeyGeneratorUtil.generateUUID());
        params.put("sign", WeChatPayUtils.genPackageSign(params, appKey));
        String resultStr = null;
        try {
            resultStr = HttpClientUtils.simplePostXMLInvoke(queryOrderUrl, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        WeChatQueryOrderResult result = WeChatPayUtils.xmlToWeChatQueryOrderResult(resultStr);
        if (result != null && result.getReturnCode().equals(SUCCESS) && result.getResultCode().equals(SUCCESS) && result.getTradeState().equals(SUCCESS))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    @Override
    public Map<String, String> wapShareSign(String url) {
        String ticket = null;
        String accessToken = null;
        String now = LocalDateTimeUtil.getCurrentTimestamp();
        WeChatToken ticketToken = weChatTokenService.getWeChatTokenByType(WeChatTokenTypeStatus.JS_API_TICKET);
        if (ticketToken != null) {
            LocalDateTime date = LocalDateTimeUtil.parseLocalDateTime(ticketToken.getUpdateTime());
            if (date != null && date.plusSeconds(6800).isAfter(LocalDateTime.now())) {
                ticket = ticketToken.getContent();
            }
        }
        if (StringUtils.isEmpty(ticket)) {
            WeChatToken weChatToken = weChatTokenService.getWeChatTokenByType(WeChatTokenTypeStatus.ACCESS_TOKEN);
            if (weChatToken != null) {
                LocalDateTime date = LocalDateTimeUtil.parseLocalDateTime(weChatToken.getUpdateTime());
                if (date != null) {
                    if (date.plusSeconds(6800).isAfter(LocalDateTime.now())) {
                        accessToken = weChatToken.getContent();
                    }
                }
            }
            if (StringUtils.isEmpty(accessToken)) {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "client_credential");
                params.put("appid", wapAppId);
                params.put("secret", wapAppSecret);
                String result = null;
                try {
                    result = HttpClientUtils.simpleGetInvoke(wapAccessTokenUrl, params);
                } catch (Exception e) {
                    log.error(e.getMessage(), e.getCause());
                }
                accessToken = (String) JSONUtil.parseObject(result).get("access_token");
                if (weChatToken == null) {
                    weChatToken = new WeChatToken();
                    weChatToken.setContent(accessToken).setType(WeChatTokenTypeStatus.ACCESS_TOKEN.getIndex());
                    weChatTokenService.saveWeChatToken(weChatToken);
                } else {
                    weChatToken.setContent(accessToken);
                    weChatTokenService.updateWeChatToken(weChatToken);
                }
            }
            Map<String, String> params = new HashMap<>();
            params.put("access_token", accessToken);
            params.put("type", "jsapi");
            try {
                String result = HttpClientUtils.simpleGetInvoke(wapTicketUrl, params);
                ticket = (String) JSONUtil.parseObject(result).get("ticket");
                if (ticketToken == null) {
                    ticketToken = new WeChatToken();
                    ticketToken.setContent(ticket).setType(WeChatTokenTypeStatus.JS_API_TICKET.getIndex());
                    weChatTokenService.saveWeChatToken(ticketToken);
                } else {
                    ticketToken.setContent(ticket);
                    weChatTokenService.updateWeChatToken(ticketToken);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e.getCause());
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("jsapi_ticket", ticket);
        params.put("url", url);
        params.put("noncestr", UniqueKeyGeneratorUtil.generateToken());
        params.put("timestamp", String.valueOf(new Date().getTime()));
        String sign = WeChatPayUtils.wapShareSign(params);
        params.put("sign", sign);
        params.remove("jsapi_ticket");
        params.remove("url");
        params.put("appid", wapAppId);
        return params;
    }

    @Override
    public String getWapOpenId(@NotNull String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", wapAppId);
        map.put("secret", wapAppSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        String result = HttpClientUtils.post(authUrl, map);
        return (String) JSONUtil.parseObject(result).get("openid");
    }

    private WeChatPayUnifiedOrderResult unifiedOrder(WeChatPayUnifiedOrderParams params, String key) throws Exception {
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", params.getAppId());
        packageParams.put("body", params.getBody());
        packageParams.put("mch_id", params.getMchId());
        packageParams.put("nonce_str", params.getNonceStr());
        packageParams.put("notify_url", params.getNotifyUrl());
        packageParams.put("out_trade_no", params.getOutTradeNo());
        packageParams.put("spbill_create_ip", params.getSpbillCreateIp());
        packageParams.put("total_fee", params.getTotalFee().toString());
        packageParams.put("trade_type", params.getTradeType());
        if (StringUtils.isNotEmpty(params.getOpenId()))
            packageParams.put("openid", params.getOpenId());
        packageParams.put("sign", WeChatPayUtils.genPackageSign(packageParams, key));
        String resultStr = HttpClientUtils.simplePostXMLInvoke(unifiedOrderUrl, packageParams);
        return WeChatPayUtils.xmlToWeChatPayUnifiedOrderResult(resultStr);
    }
}
