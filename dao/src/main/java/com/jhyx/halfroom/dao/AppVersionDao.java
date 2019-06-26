package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.AppVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppVersionDao {
    List<AppVersion> select_app_version_by_os(@Param("os") String os);
}
