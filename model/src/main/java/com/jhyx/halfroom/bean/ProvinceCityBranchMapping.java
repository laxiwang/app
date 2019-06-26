package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProvinceCityBranchMapping {
    private Integer id;
    private String province;
    private String city;
    private Integer branchSaleId;
}
