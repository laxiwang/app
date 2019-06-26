package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Manual;
import com.jhyx.halfroom.dao.ManualDao;
import com.jhyx.halfroom.service.ManualService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManualServiceImpl implements ManualService {
    private ManualDao manualDao;

    @Override
    public List<Manual> getManualByType(Integer type) {
        return manualDao.select_manual_by_type(type);
    }
}
