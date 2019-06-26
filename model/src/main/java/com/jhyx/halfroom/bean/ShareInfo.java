package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShareInfo {
    private Integer id;
    private String bgImageName;
    private Integer bookId;
    private String redirectUrl;
    private Boolean isSaleAmbassador;
    private Integer qrX;
    private Integer qrY;
    private Integer qrW;
    private Integer qrH;
    private String fontStyle;
    private String fontStyleNext;
    private Integer fontSize;
    private Integer fontSizeNext;
    private Integer fontX;
    private Integer fontXNext;
    private Integer fontY;
    private Integer fontYNext;
    private Integer fontColorR;
    private Integer fontColorRNext;
    private Integer fontColorG;
    private Integer fontColorGNext;
    private Integer fontColorB;
    private Integer fontColorBNext;
    private Integer type;
}
