package com.jhyx.halfroom.vo.app;

import com.jhyx.halfroom.vo.common.CourseTestVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CoursePlayDetailVo {
    private Integer id;
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String bookName;
    private String bookChapterPartName;
    private String backgroundImageUrl;
    private String faceThumbUrl;
    private String playUrl;
    private String speaker;
    private boolean purchase;
    private Integer timeEnd;
    private Integer timeSum;
    private Long commentCount;
    private String imageUrl;
    private List<CourseTestVo> courseTestList;
    private String bookChapterPartShareBriefTitle;
    private String bookChapterPartShareBrief;
    //答题的情况，0表示未答题，1表达答对部分，2表示答对全部
    private Integer answerStatus;
}
