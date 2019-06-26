package com.jhyx.halfroom.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCommentResultVo {
    private boolean flag;
    private Long commentId;
}
