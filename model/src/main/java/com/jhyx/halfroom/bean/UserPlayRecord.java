package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPlayRecord {
    private Long id;
    private Long userId;
    private Integer bookId;
    private Integer chapterId;
    private Integer partId;
    private Integer playCount;
    private Integer playTimeEnd;
    private String createTime;
    private String updateTime;
}
