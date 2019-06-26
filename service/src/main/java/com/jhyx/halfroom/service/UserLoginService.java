package com.jhyx.halfroom.service;

public interface UserLoginService {
    void saveOrUpdateUserLogin(Long userId, String token);

    Long getUserLoginByToken(String token);
}
