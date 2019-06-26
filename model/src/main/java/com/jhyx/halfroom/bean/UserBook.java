package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserBook {
    private Long id;
    private Long userId;
    private Integer bookId;
    private Integer type;
    private String startTime;
    private String endTime;
}
