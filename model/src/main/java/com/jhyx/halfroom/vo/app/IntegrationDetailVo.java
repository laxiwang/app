package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class IntegrationDetailVo {
    private Integer integrationSum;
    private List<IntegrationOriginVo> integrationOrigin;
}
