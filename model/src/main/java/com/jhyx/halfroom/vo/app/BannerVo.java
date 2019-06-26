package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BannerVo {
    private String imgUrl;
    private String redirectUrl;
    private Integer type;
}
