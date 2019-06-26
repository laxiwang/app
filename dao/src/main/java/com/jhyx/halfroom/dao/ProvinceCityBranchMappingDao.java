package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.ProvinceCityBranchMapping;
import org.apache.ibatis.annotations.Param;

public interface ProvinceCityBranchMappingDao {
    ProvinceCityBranchMapping select_province_city_branch_mapping_by_province_and_city(@Param("province") String province,
                                                                                       @Param("city") String city);
}
