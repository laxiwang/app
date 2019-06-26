package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BranchSaler;

import java.util.Collection;
import java.util.List;

public interface BranchSalerService {
    //根据名称获取市级分会
    List<BranchSaler> getCityBranchSalerByName(String name);

    BranchSaler getProvinceBranchSalerByName(String name);

    BranchSaler getBranchSalerById(Integer id);

    List<BranchSaler> getBranchSalerByIds(Collection<Integer> ids);
}
