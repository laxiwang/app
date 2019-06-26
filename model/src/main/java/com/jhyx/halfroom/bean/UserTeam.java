package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserTeam {
    private Long id;
    private Long userId;
    private Long teamId;
    private Integer userType;
    private String createTime;
    private String updateTime;
}
