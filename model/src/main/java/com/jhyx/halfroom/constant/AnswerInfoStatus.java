package com.jhyx.halfroom.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnswerInfoStatus {
    NO_ANSWER(0, "未答题"), SECTION_ANSWER_CORRECT(1, "答对部分"), ALL_ANSWER_CORRECT(2, "答对全部");
    private Integer index;
    private String msg;
}
