package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserPlayRecord;
import com.jhyx.halfroom.vo.app.UserPlayRecordCountVo;

import java.util.List;

public interface UserPlayRecordService {
    Integer saveUserPlayRecord(UserPlayRecord userPlayRecord);

    UserPlayRecord getUserPlayRecordByUserIdAndBookIdAndChapterIdAndPartId(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    Integer updateUserPlayRecord(UserPlayRecord userPlayRecord);

    List<UserPlayRecord> getUserPlayRecordByUserId(Long userId);

    List<UserPlayRecordCountVo> getUserPlayRecordListByBookIdAndBookChapterId(Integer bookId, Integer bookChapterId);

    Long getBookChapterPartPlayRecordCount(Integer bookId, Integer bookChapterId, Integer bookChapterPartId);
}
