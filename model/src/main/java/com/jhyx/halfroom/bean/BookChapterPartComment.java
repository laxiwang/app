package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartComment {
    private Long id;
    private Integer bookId;
    private Integer chapterId;
    private Integer partId;
    private String content;
    private Long userId;
    private Long atCommentId;
    private Integer type;
    private Integer timeSum;
    private Boolean isHotComment;
    private String createTime;
    private String updateTime;
}
