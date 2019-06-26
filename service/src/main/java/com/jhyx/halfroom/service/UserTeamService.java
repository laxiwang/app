package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserTeam;

import java.util.List;

public interface UserTeamService {
    UserTeam getUserTeamByUserId(Long userId);

    void saveUserTeam(UserTeam userTeam);

    List<UserTeam> getUserTeamByTeamId(Long teamId);
}
