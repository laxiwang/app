package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BranchInvite;
import com.jhyx.halfroom.constant.BranchInviteStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BranchInviteDao {
    List<BranchInvite> select_saler_invite_log_by_user_id_and_status(@Param("userId") Long userId, @Param("inviteStatus") BranchInviteStatus status);

    Integer update_saler_invite_log(BranchInvite branchInvite);

    BranchInvite select_saler_invite_log_by_id(@Param("id") Integer id);
}
