package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class BookPresentationVo {
    private String courseFaceThumbUrl;
    private String faceThumbUrl;
    private String speakerBriefUrl;
    private String briefUrl;
    private String outLineUrl;
    private String name;
    private String speaker;
    private boolean purchase;
    private String purchaseNotes;
    private BigDecimal price;
    private Integer sectionCount;
    private List<BookPresentationChapterPartVo> list;
}
