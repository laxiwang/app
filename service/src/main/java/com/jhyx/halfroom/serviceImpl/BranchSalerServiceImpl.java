package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BranchSaler;
import com.jhyx.halfroom.constant.BranchSalerStatus;
import com.jhyx.halfroom.dao.BranchSalerDao;
import com.jhyx.halfroom.service.BranchSalerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BranchSalerServiceImpl implements BranchSalerService {
    private BranchSalerDao branchSalerDao;

    @Override
    public List<BranchSaler> getCityBranchSalerByName(String name) {
        return branchSalerDao.select_branchsaler_by_simplename_and_level(name, BranchSalerStatus.CITY.getIndex());
    }

    @Override
    public BranchSaler getBranchSalerById(Integer id) {
        return branchSalerDao.select_branchsaler_by_id(id);
    }

    @Override
    public List<BranchSaler> getBranchSalerByIds(Collection<Integer> ids) {
        return branchSalerDao.select_branchsaler_by_ids(ids);
    }

    @Override
    public BranchSaler getProvinceBranchSalerByName(String name) {
        List<BranchSaler> list = branchSalerDao.select_branchsaler_by_simplename_and_level(name, BranchSalerStatus.PROVINCE.getIndex());
        if(list == null || list.size() == 0)
            return null;
        else
            return list.get(0);
    }
}
