package com.jhyx.halfroom.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Book {
    private Integer id;
    private String name;
    private Integer category;
    private BigDecimal price;
    private String brief;
    private String briefTitle;
    private String speaker;
    private String speakerBrief;
    private String speakerBriefUrl;
    private String faceThumbUrl;
    private String smallFaceThumbUrl;
    private String appFaceThumbUrl;
    private Integer ageMin;
    private Integer ageMax;
    private String briefUrl;
    private String outlineUrl;
    private String grade;
    private Integer needIntegral;
    private String purchaseNotes;
    private String bookChapterPartShareBrief;
    private String bookChapterPartShareBriefTitle;
    private Integer courseCount;
    private Integer updateCourseCount;
    private Integer status;
    private String createTime;
    private String updateTime;
}
