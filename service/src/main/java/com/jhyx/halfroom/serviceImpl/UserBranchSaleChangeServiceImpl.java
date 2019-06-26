package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserBranchSaleChange;
import com.jhyx.halfroom.dao.UserBranchSaleChangeDao;
import com.jhyx.halfroom.service.UserBranchSaleChangeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserBranchSaleChangeServiceImpl implements UserBranchSaleChangeService {
    private UserBranchSaleChangeDao userBranchSaleChangeDao;

    @Override
    public void saveUserBranchSaleChange(UserBranchSaleChange userBranchSaleChange) {
        userBranchSaleChangeDao.insert_into_user_branchsaler_change(userBranchSaleChange);
    }
}
