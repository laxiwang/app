package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookPresentationInfoVo {
    private String name;
    private String faceThumbUrl;
    private Integer courseCount;
    private BigDecimal price;
    private String purchaseNotes;
    private Boolean permissions;
}
