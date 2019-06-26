package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BranchInvite {
    private Integer id;
    private Long userId;
    private Integer branchsalerId;
    private String createtime;
    private String updatetime;
    private Integer status;
    private Integer userReadStatus;
    private String remark;
}
