package com.jhyx.halfroom.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentVo {
    private Long id;
    private String avatar;
    private String nickName;
    private boolean praise;
    private Integer praiseCount;
    private Long atCommentId;
    private String commentMsg;
    private Integer type;
    private Integer timeSum;
    private String createTime;
}
