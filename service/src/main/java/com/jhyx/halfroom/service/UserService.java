package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User getUserByPhone(String phone);

    Integer saveUser(User user);

    User getUserById(Long userId);

    Integer updateUser(User user);

    List<User> getUserListByIds(Collection<Long> ids);

    Long getUserCount();

    Long getAmbassadorSpreadUserCount(Long ambassadorId);
}
