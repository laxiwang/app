package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IntegrationOriginVo {
    private String origin;
    private String createTime;
    private String integration;
}
