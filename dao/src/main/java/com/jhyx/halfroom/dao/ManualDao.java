package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.Manual;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManualDao {
    List<Manual> select_manual_by_type(@Param("type") Integer type);
}
