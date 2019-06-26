package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserTeam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTeamDao {
    UserTeam select_activity_user_team_by_user_id(@Param("userId") Long userId);

    void insert_into_activity_user_team(UserTeam userTeam);

    List<UserTeam> select_activity_user_team_by_team_id(@Param("teamId") Long teamId);
}
