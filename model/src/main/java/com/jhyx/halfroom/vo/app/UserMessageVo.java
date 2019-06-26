package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserMessageVo {
    private Integer id;
    private Long userId;
    private Integer branchId;
    private String branchName;
    private String message;
    private String inviteTime;
}
