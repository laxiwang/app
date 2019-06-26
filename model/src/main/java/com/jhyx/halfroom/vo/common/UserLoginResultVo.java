package com.jhyx.halfroom.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginResultVo {
    private String token;
    private String imageUrl;
    private Long userId;
    private String name;
    private String avatar;
}
