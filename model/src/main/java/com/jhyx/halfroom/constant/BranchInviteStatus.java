package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BranchInviteStatus {
    WAIT_INVITE(0, "邀请中"), ACCEPT_INVITE(1, "接受邀请"),
    REFUSE_INVITE(2, "拒绝邀请"), CANCEL_INVITE(3, "取消邀请");
    private Integer index;
    private String msg;
}
