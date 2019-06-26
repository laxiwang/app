package com.jhyx.halfroom.vo.wap;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookChapterPartContentVo {
    private String title;
    private String subTitle;
    private String content;
    private String audioUrl;
    private String imageUrl;
}
