package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.ProvinceCityBranchMapping;
import com.jhyx.halfroom.dao.ProvinceCityBranchMappingDao;
import com.jhyx.halfroom.service.ProvinceCityBranchMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProvinceCityBranchMappingServiceImpl implements ProvinceCityBranchMappingService {
    private ProvinceCityBranchMappingDao provinceCityBranchMappingDao;

    @Override
    public ProvinceCityBranchMapping getProvinceCityBranchMappingByProvinceAndCity(String province, String city) {
        return provinceCityBranchMappingDao.select_province_city_branch_mapping_by_province_and_city(province, city);
    }
}
