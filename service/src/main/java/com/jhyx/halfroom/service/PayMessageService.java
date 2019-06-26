package com.jhyx.halfroom.service;

import com.jhyx.halfroom.message.DistributionMessageBean;

import javax.validation.constraints.NotNull;

public interface PayMessageService {
    void sendPaySuccessMessage(@NotNull DistributionMessageBean messageBean);
}
