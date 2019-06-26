package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapter {
    private Integer id;
    private Integer bookId;
    private Integer bookChapterId;
    private String name;
    private Integer status;
}
