package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPlayTimeSumVo {
    private Long userId;
    private Long playTime;
}
