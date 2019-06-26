package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.Manual;

import java.util.List;

public interface ManualService {
    List<Manual> getManualByType(Integer type);
}
