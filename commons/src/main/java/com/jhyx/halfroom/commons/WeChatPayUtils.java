package com.jhyx.halfroom.commons;

import com.jhyx.halfroom.wechat.WeChatPayResult;
import com.jhyx.halfroom.wechat.WeChatPayUnifiedOrderResult;
import com.jhyx.halfroom.wechat.WeChatQueryOrderResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
public class WeChatPayUtils {
    private static Map<String, String> paraFilter(Map<String, String> params) {
        Map<String, String> result = new HashMap<>();
        if (params == null || params.size() <= 0) {
            return result;
        }
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (StringUtils.isEmpty(value) || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    private static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i != keys.size() - 1) {
                builder.append(key).append("=").append(value).append("&");
            } else {
                builder.append(key).append("=").append(value);
            }
        }
        return builder.toString();
    }

    public static WeChatPayResult xmlToWeChatPayResult(String resultStr) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(resultStr);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        Element rootEle = doc.getRootElement();
        WeChatPayResult result = new WeChatPayResult();
        result.setReturnCode(rootEle.element("return_code").getText());
        result.setReturnMsg(rootEle.element("return_msg") == null ? StringUtils.EMPTY : rootEle.element("return_msg").getText());
        if (rootEle.element("appid") != null) {
            result.setAppid(rootEle.element("appid").getText());
        }
        if (rootEle.element("mch_id") != null) {
            result.setMchId(rootEle.element("mch_id").getText());
        }
        if (rootEle.element("nonce_str") != null) {
            result.setNonceStr(rootEle.element("nonce_str").getText());
        }
        if (rootEle.element("sign") != null) {
            result.setSign(rootEle.element("sign").getText());
        }
        if (rootEle.element("result_code") != null) {
            result.setResultCode(rootEle.element("result_code").getText());
        }
        if (rootEle.element("err_code") != null) {
            result.setErrCode(rootEle.element("err_code").getText());
        }
        if (rootEle.element("err_code_des") != null) {
            result.setErrCodeDes(rootEle.element("err_code_des").getText());
        }
        if (rootEle.element("openid") != null) {
            result.setOpenid(rootEle.element("openid").getText());
        }
        if (rootEle.element("is_subscribe") != null) {
            result.setIsSubscribe(rootEle.element("is_subscribe").getText());
        }
        if (rootEle.element("trade_type") != null) {
            result.setTradeType(rootEle.element("trade_type").getText());
        }

        if (rootEle.element("bank_type") != null) {
            result.setBankType(rootEle.element("bank_type").getText());
        }
        if (rootEle.element("total_fee") != null) {
            result.setTotalFee(Integer.parseInt(rootEle.element("total_fee")
                    .getText()));
        }
        if (rootEle.element("cash_fee") != null) {
            result.setCashFee(Integer.parseInt(rootEle.element("cash_fee")
                    .getText()));
        }
        if (rootEle.element("transaction_id") != null) {
            result.setTransactionId(rootEle.element("transaction_id").getText());
        }
        if (rootEle.element("out_trade_no") != null) {
            result.setOutTradeNo(rootEle.element("out_trade_no").getText());
        }
        if (rootEle.element("time_end") != null) {
            result.setTimeEnd(rootEle.element("time_end").getText());
        }
        return result;
    }

    public static WeChatPayUnifiedOrderResult xmlToWeChatPayUnifiedOrderResult(String resultStr) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(resultStr);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
        Element rootEle = doc.getRootElement();
        WeChatPayUnifiedOrderResult result = new WeChatPayUnifiedOrderResult();
        result.setReturnCode(rootEle.element("return_code").getText());
        result.setReturnMsg(rootEle.element("return_msg") == null ? StringUtils.EMPTY : rootEle.element("return_msg").getText());
        if (rootEle.element("appid") != null) {
            result.setAppId(rootEle.element("appid").getText());
        }
        if (rootEle.element("mch_id") != null) {
            String mchId = rootEle.element("mch_id").getText();
            result.setMchId(mchId);
        }
        if (rootEle.element("nonce_str") != null) {
            String nonceStr = rootEle.element("nonce_str").getText();
            result.setNonceStr(nonceStr);
        }
        if (rootEle.element("sign") != null) {
            String sign = rootEle.element("sign").getText();
            result.setSign(sign);
        }
        if (rootEle.element("result_code") != null) {
            result.setResultCode(rootEle.element("result_code").getText());
        }
        if (rootEle.element("prepay_id") != null) {
            result.setPrepayId(rootEle.element("prepay_id").getText());
        }
        if (rootEle.element("trade_type") != null) {
            result.setTradeType(rootEle.element("trade_type").getText());
        }
        if (rootEle.element("err_code") != null) {
            result.setErrorCode(rootEle.element("err_code").getText());
        }
        if (rootEle.element("err_code_des") != null) {
            result.setErrorCodeDesc(rootEle.element("err_code_des").getText());
        }
        return result;
    }

    public static WeChatQueryOrderResult xmlToWeChatQueryOrderResult(String resultStr) {
        Document doc;
        try {
            doc = DocumentHelper.parseText(resultStr);
        } catch (Exception e) {
            return null;
        }
        Element rootEle = doc.getRootElement();
        WeChatQueryOrderResult result = new WeChatQueryOrderResult();
        result.setReturnCode(rootEle.element("return_code").getText()).setReturnMsg(rootEle.element("return_msg") == null ? StringUtils.EMPTY : rootEle.element("return_msg").getText());
        if (rootEle.element("appid") != null) {
            String appId = rootEle.element("appid").getText();
            result.setAppid(appId);
        }
        if (rootEle.element("mch_id") != null) {
            String mchId = rootEle.element("mch_id").getText();
            result.setMchId(mchId);
        }
        if (rootEle.element("nonce_str") != null) {
            Element element = rootEle.element("nonce_str");
            result.setNonceStr(element.getText());
        }
        if (rootEle.element("sign") != null) {
            result.setSign(rootEle.element("sign").getText());
        }
        if (rootEle.element("result_code") != null) {
            String resultCode = rootEle.element("result_code").getText();
            result.setResultCode(resultCode);
        }
        if (rootEle.element("err_code") != null) {
            String errCode = rootEle.element("err_code").getText();
            result.setErrCode(errCode);
        }
        if (rootEle.element("err_code_des") != null) {
            String errCodeDes = rootEle.element("err_code_des").getText();
            result.setErrCodeDes(errCodeDes);
        }
        if (rootEle.element("openid") != null) {
            String openId = rootEle.element("openid").getText();
            result.setOpenid(openId);
        }
        if (rootEle.element("trade_type") != null) {
            result.setTradeType(rootEle.element("trade_type").getText());
        }
        if (rootEle.element("trade_state") != null) {
            result.setTradeState(rootEle.element("trade_state").getText());
        }
        if (rootEle.element("bank_type") != null) {
            result.setBankType(rootEle.element("bank_type").getText());
        }
        if (rootEle.element("total_fee") != null) {
            result.setTotalFee(rootEle.element("total_fee").getText());
        }
        if (rootEle.element("cash_fee") != null) {
            result.setCashFee(rootEle.element("cash_fee").getText());
        }
        if (rootEle.element("transaction_id") != null) {
            result.setTransactionId(rootEle.element("transaction_id").getText());
        }
        if (rootEle.element("out_trade_no") != null) {
            String outTradeNo = rootEle.element("out_trade_no").getText();
            result.setOutTradeNo(outTradeNo);
        }
        if (rootEle.element("time_end") != null) {
            String timeEnd = rootEle.element("time_end").getText();
            result.setTimeEnd(timeEnd);
        }
        if (rootEle.element("trade_state_desc") != null) {
            result.setTradeStateDesc(rootEle.element("trade_state_desc").getText());
        }
        return result;
    }

    public static boolean verifyMapParam(Map<String, String> params, String key, String weChatSign) {
        String sign = WeChatPayUtils.genPackageSign(params, key);
        return weChatSign.equals(sign);
    }

    public static String getXmlFromRequest(HttpServletRequest request) throws Exception {
        String reqXml;
        reqXml = request.getQueryString();
        if (StringUtils.isBlank(reqXml)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            reqXml = sb.toString();
        }
        return reqXml;
    }

    public static String map2Xml(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">");
            sb.append(entry.getValue());
            sb.append("</").append(entry.getKey()).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String genPackageSign(Map<String, String> packageParams, String key) {
        packageParams = paraFilter(packageParams);
        String paramsStr = createLinkString(packageParams);
        paramsStr += "&key=" + key;
        return weChatMD5Encode(paramsStr).toUpperCase();
    }

    public static String wapShareSign(Map<String, String> packageParams) {
        packageParams = paraFilter(packageParams);
        String paramsStr = createLinkString(packageParams);
        return DigestUtils.sha1Hex(paramsStr);
    }

    private static String weChatMD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(resultString.getBytes(StandardCharsets.UTF_8));
            resultString = MD5Util.byteArrayToHexString(md.digest());
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return resultString;
    }
}
