package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    private Long userId;
    private String avatar;
    private String name;
    private String gender;
    private String birthday;
    private String grade;
    private String favouriteSubject;
}
