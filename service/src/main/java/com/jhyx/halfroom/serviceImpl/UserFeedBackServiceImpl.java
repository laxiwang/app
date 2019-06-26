package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserFeedBack;
import com.jhyx.halfroom.dao.UserFeedBackDao;
import com.jhyx.halfroom.service.UserFeedBackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserFeedBackServiceImpl implements UserFeedBackService {
    private UserFeedBackDao userFeedBackDao;

    @Override
    public Integer saveUserFeedBack(UserFeedBack userFeedBack) {
        return userFeedBackDao.insert_into_user_feed_back(userFeedBack);
    }
}
