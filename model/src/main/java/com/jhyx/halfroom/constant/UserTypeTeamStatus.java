package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeTeamStatus {
    TEAM_GENERAL(0, "队员"), TEAM_MASTER(1, "队长");
    private Integer index;
    private String msg;
}
