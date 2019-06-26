package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private Integer mappingNewAdd;
    private String name;
    private String favouriteSubject;
    private String phone;
    private String gender;
    private String headImage;
    private String birthday;
    private String grade;
    private String country;
    private String province;
    private String city;
    private Integer registerEntry;
    private Long registerIntroducer;
    private Integer branchsaler;
    private Integer status;
    private Integer role;
    private String remark;
    private String updateTime;
    private String createTime;
}
