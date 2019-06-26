package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserReadBranchInviteStatus {
    READ(1, "用户已读"), UN_READ(0, "用户未读");
    private Integer index;
    private String msg;
}
