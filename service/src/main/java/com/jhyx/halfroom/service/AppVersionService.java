package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.AppVersion;

import java.util.List;

public interface AppVersionService {
    List<AppVersion> getAppVersionByOS(String os);
}
