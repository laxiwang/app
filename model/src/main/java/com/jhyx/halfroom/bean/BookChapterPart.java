package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPart {
    private Integer id;
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String name;
    private String experienceUrl;
    private String vipUrl;
    private String imgUrl;
    private String appUrl;
    private Integer timeSum;
    private Integer type;
    private Integer state;
    private Integer status;
}
