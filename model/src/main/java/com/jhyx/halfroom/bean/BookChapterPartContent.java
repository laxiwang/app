package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartContent {
    private Integer id;
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String title;
    private String subTitle;
    private String content;
    private String createTime;
}
