package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.AppVersion;
import com.jhyx.halfroom.dao.AppVersionDao;
import com.jhyx.halfroom.service.AppVersionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppVersionServiceImpl implements AppVersionService {
    private AppVersionDao appVersionDao;

    @Override
    public List<AppVersion> getAppVersionByOS(String os) {
        return appVersionDao.select_app_version_by_os(os);
    }
}
