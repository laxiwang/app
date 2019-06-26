package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.RedisKeyPrefix;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.PhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneServiceImpl implements PhoneService {
    private final CommonService commonService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public Boolean sendLoginCode(String phone) {
        try {
            String randomCode = UniqueKeyGeneratorUtil.random(4);
            commonService.sendMessageCode(phone, randomCode);
            redisTemplate.opsForValue().set(RedisKeyPrefix.PHONE_CODE + phone, randomCode, 10, TimeUnit.MINUTES);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return Boolean.FALSE;
        }
    }

    @Override
    public String getPhoneCodeByPhone(String phone) {
        return redisTemplate.opsForValue().get(RedisKeyPrefix.PHONE_CODE + phone);
    }
}