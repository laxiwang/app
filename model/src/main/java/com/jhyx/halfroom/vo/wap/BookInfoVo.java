package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class BookInfoVo {
    private String faceThumbUrl;
    private String briefUrl;
    private String speakerBriefUrl;
    private String outlineUrl;
    private String name;
    private BigDecimal price;
    private Integer courseCount;
    //0未登录，1体验中  2体验过期  3已付费
    private Integer status;
    private List<BookInfoBookChapterPartVo> medias;
    private String purchaseNotes;
}
