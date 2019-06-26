package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserPlayTimeSum;
import com.jhyx.halfroom.vo.app.UserPlayTimeSumVo;

import java.util.List;

public interface UserPlayTimeSumService {
    List<UserPlayTimeSum> getUserPlayTimeSumByUserIdAndBookIdAndBookChapterIdAndBookChapterPartId(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    void saveUserPlayTimeSum(UserPlayTimeSum userPlayTimeSum);

    void updateUserPlayTimeSum(UserPlayTimeSum userPlayTimeSum);

    List<UserPlayTimeSumVo> getUserPlayTimeSum();
}
