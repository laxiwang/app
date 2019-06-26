package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BranchInvite;
import com.jhyx.halfroom.constant.BranchInviteStatus;

import java.util.List;

public interface BranchInviteService {
    List<BranchInvite> getBranchInviteListByUserIdAndStatus(Long UserId, BranchInviteStatus status);

    Integer updateBranchInvite(BranchInvite branchInvite);

    BranchInvite getBranchInviteById(Integer id);
}
