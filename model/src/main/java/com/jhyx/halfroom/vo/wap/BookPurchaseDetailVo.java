package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookPurchaseDetailVo {
    private Integer bookId;
    private String name;
    private Integer courseCount;
    private BigDecimal price;
    private String faceThumbUrl;
}
