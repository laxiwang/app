package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartCommentPraise {
    private Long id;
    private Long bookChapterPartCommentId;
    private Long userId;
    private String createTime;
}
