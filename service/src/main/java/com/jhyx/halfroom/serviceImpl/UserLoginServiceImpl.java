package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.constant.RedisKeyPrefix;
import com.jhyx.halfroom.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveOrUpdateUserLogin(Long userId, String token) {
        String oldToken = redisTemplate.opsForValue().get(RedisKeyPrefix.USER_LOGIN_USER_ID + userId);
        redisTemplate.opsForValue().set(RedisKeyPrefix.USER_LOGIN_USER_ID + userId, token, 30, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(RedisKeyPrefix.USER_LOGIN_TOKEN + token, String.valueOf(userId), 30, TimeUnit.DAYS);
        if (StringUtils.isNotEmpty(oldToken)) {
            redisTemplate.delete(RedisKeyPrefix.USER_LOGIN_TOKEN + oldToken);
        }
    }

    @Override
    public Long getUserLoginByToken(String token) {
        String userId = redisTemplate.opsForValue().get(RedisKeyPrefix.USER_LOGIN_TOKEN + token);
        return StringUtils.isEmpty(userId) ? null : Long.parseLong(userId);
    }
}
