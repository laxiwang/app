package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.Manual;
import com.jhyx.halfroom.bean.UserManual;

import java.util.Collection;
import java.util.List;

public interface UserManualService {
    List<UserManual> getUserManualByUserId(Long userId);

    List<Manual> getManualListByUserId(Long userId);

    void batchSaveUserManualList(Collection<UserManual> userManualList);
}
