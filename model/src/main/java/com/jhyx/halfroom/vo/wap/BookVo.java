package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookVo {
    private Integer bookId;
    private Integer category;
    private String name;
    private String speaker;
    private String brief;
    private BigDecimal price;
    private Integer ageMax;
    private Integer ageMin;
    private Integer courseCount;
    private String faceThumbUrl;
    private Integer type;
    private Integer page;
    private String url;
}
