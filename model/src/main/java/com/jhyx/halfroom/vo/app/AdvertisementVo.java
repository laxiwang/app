package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AdvertisementVo {
    private String imgUrl;
    private String redirectUrl;
}
