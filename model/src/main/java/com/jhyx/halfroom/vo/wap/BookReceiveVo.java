package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookReceiveVo {
    private Integer bookId;
    private String faceThumbUrl;
    private String name;
    private String grade;
    private Integer courseCount;
    private BigDecimal price;
    private String speaker;
    private Integer status;
}
