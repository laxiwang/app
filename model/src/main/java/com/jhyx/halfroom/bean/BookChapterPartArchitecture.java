package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartArchitecture {
    private Integer id;
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String imgUrl;
    private String title;
    private String option;
    private String answer;
    private Integer type;
    private Integer status;
    private String createTime;
}
