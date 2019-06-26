package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.ProvinceCityBranchMapping;

public interface ProvinceCityBranchMappingService {
    ProvinceCityBranchMapping getProvinceCityBranchMappingByProvinceAndCity(String province, String city);
}
