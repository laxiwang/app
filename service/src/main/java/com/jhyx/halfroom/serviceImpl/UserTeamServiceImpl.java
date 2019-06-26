package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserTeam;
import com.jhyx.halfroom.dao.UserTeamDao;
import com.jhyx.halfroom.service.UserTeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserTeamServiceImpl implements UserTeamService {
    private UserTeamDao userTeamDao;

    @Override
    public UserTeam getUserTeamByUserId(Long userId) {
        return userTeamDao.select_activity_user_team_by_user_id(userId);
    }

    @Override
    public void saveUserTeam(UserTeam userTeam) {
        userTeamDao.insert_into_activity_user_team(userTeam);
    }

    @Override
    public List<UserTeam> getUserTeamByTeamId(Long teamId) {
        return userTeamDao.select_activity_user_team_by_team_id(teamId);
    }
}
