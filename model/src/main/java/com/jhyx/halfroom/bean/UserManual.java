package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserManual {
    private Long id;
    private Long userId;
    private Integer manualId;
    private String createTime;
    private String updateTime;
}
