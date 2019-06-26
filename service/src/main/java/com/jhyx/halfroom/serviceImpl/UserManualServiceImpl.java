package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Manual;
import com.jhyx.halfroom.bean.UserManual;
import com.jhyx.halfroom.dao.UserManualDao;
import com.jhyx.halfroom.service.UserManualService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserManualServiceImpl implements UserManualService {
    private UserManualDao userManualDao;

    @Override
    public List<UserManual> getUserManualByUserId(Long userId) {
        return userManualDao.select_user_manual_by_user_id(userId);
    }

    @Override
    public void batchSaveUserManualList(Collection<UserManual> userManualList) {
        if (userManualList != null && userManualList.size() > 0) {
            userManualDao.batch_insert_into_user_manual(userManualList);
        }
    }

    @Override
    public List<Manual> getManualListByUserId(Long userId) {
        return userManualDao.select_user_manual_and_manual_by_user_id(userId);
    }
}
