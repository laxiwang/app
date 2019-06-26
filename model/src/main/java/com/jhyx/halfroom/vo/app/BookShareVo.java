package com.jhyx.halfroom.vo.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookShareVo {
    private Long userId;
    private String imgUrl;
    private String title;
    private String content;
    private boolean saleAmbassador;
}
