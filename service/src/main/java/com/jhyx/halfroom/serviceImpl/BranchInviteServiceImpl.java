package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BranchInvite;
import com.jhyx.halfroom.constant.BranchInviteStatus;
import com.jhyx.halfroom.dao.BranchInviteDao;
import com.jhyx.halfroom.service.BranchInviteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BranchInviteServiceImpl implements BranchInviteService {
    private BranchInviteDao branchInviteDao;

    @Override
    public List<BranchInvite> getBranchInviteListByUserIdAndStatus(Long userId, BranchInviteStatus status) {
        return branchInviteDao.select_saler_invite_log_by_user_id_and_status(userId, status);
    }

    @Override
    public Integer updateBranchInvite(BranchInvite branchInvite) {
        return branchInviteDao.update_saler_invite_log(branchInvite);
    }

    @Override
    public BranchInvite getBranchInviteById(Integer id) {
        return branchInviteDao.select_saler_invite_log_by_id(id);
    }
}
