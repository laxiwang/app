package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookVo {
    private Integer bookId;
    private String faceThumbUrl;
    private String name;
    private Integer ageMax;
    private Integer ageMin;
    private BigDecimal money;
    private Long playSum;
    private String speaker;
    private String speakerBrief;
    private Integer bookChapterPartCount;
    private String brief;
    private boolean purchase;
}
