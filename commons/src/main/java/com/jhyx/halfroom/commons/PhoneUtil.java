package com.jhyx.halfroom.commons;

import com.alibaba.fastjson.JSONObject;
import com.jhyx.halfroom.commons.httpclient.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PhoneUtil {
    private static final String url = "http://apis.juhe.cn/mobile/get?key=dcf3235077a4bbb57ae5f400da22e59f&phone=";
    private static final String host = "http://mobsec-dianhua.baidu.com";
    private static final String path = "/dianhua_api/open/location";
    private static final String method = "GET";

    public static Map<String, String> getProvinceAndCityByPhone(String phone) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            Map<String, String> map;
            map = getProvinceAndCityByBaiDu(phone);
            if (map != null && map.size() > 0) {
                result = map;
                break;
            }
            map = getProvinceAndCityByJuHe(phone);
            if (map != null && map.size() > 0) {
                result = map;
                break;
            }
        }
        return result;
    }

    private static Map<String, String> getProvinceAndCityByJuHe(String phone) {
        Map<String, String> result = new HashMap<>();
        String str = url + phone;
        try {
            String value = HttpClientUtils.get(str);
            JSONObject obj = JSONUtil.parseObject(value);
            if (obj.get("resultcode").equals("200")) {
                obj = JSONUtil.parseObject(obj.get("result").toString());
                String province = (String) obj.get("province");
                String city = (String) obj.get("city");
                if (StringUtils.isEmpty(city)) {
                    city = province;
                }
                result.put("city", city);
                result.put("province", province);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return result;
    }

    private static Map<String, String> getProvinceAndCityByBaiDu(String phone) {
        Map<String, String> result = new HashMap<>();
        try {
            Map<String, String> headers = new HashMap<>();
            Map<String, String> querys = new HashMap<>();
            querys.put("tel", phone);
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String phoneInfo = EntityUtils.toString(response.getEntity());
            int city = phoneInfo.indexOf("city");
            int province = phoneInfo.indexOf("province");
            int type = phoneInfo.indexOf("type");
            if (city != -1 && province != -1 && type != -1) {
                result.put("city", phoneInfo.substring(city + 7, province - 5));
                result.put("province", phoneInfo.substring(province + 11, type - 3));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
        }
        return result;
    }
}
