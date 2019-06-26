package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPurchaseBookVo {
    private Integer bookId;
    private String faceThumbUrl;
    private String name;
    private String grade;
    private String speaker;
    private Integer sectionCount;
    private Integer page;
    private String url;
    private Integer type;
}
