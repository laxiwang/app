package com.jhyx.halfroom.vo.wap;

import com.jhyx.halfroom.vo.common.CourseTestVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CourseDetailVo {
    private Integer bookId;
    private Integer bookChapterId;
    private Integer bookChapterPartId;
    private String bookChapterPartName;
    private String backgroundImageUrl;
    private String playUrl;
    private Integer duration;
    private boolean video;
    private String imageUrl;
    private List<CourseTestVo> courseTestList;
    //答题的情况，0表示未答题，1表达答对部分，2表示答对全部
    private Integer answerStatus;
}
