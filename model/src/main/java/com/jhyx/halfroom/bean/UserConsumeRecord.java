package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserConsumeRecord {
    private Integer id;
    private Long userId;
    private Integer bookId;
    private Integer consumePoint;
    private String createTime;
}
