package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.User;
import com.jhyx.halfroom.dao.UserDao;
import com.jhyx.halfroom.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Override
    public Long getAmbassadorSpreadUserCount(Long ambassadorId) {
        return userDao.select_user_count_by_register_introducer(ambassadorId);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userDao.select_user_by_phone(phone);
    }

    @Override
    public Integer saveUser(User user) {
        return userDao.insert_into_user(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.select_user_by_id(userId);
    }

    @Override
    public List<User> getUserListByIds(Collection<Long> ids) {
        return userDao.select_user_by_ids(ids);
    }

    @Override
    public Integer updateUser(User user) {
        return userDao.update_user(user);
    }

    @Override
    public Long getUserCount() {
        return userDao.select_user_count();
    }
}
