package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BranchSalerStatus {
    HEADQUARTERS(0, "总部"), PROVINCE(1, "省级"), CITY(2, "市级"), COUNTRY(3, "区县级"), FREE(4, "自由分会");
    private Integer index;
    private String msg;
}
