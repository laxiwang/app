package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserPlayRecord;
import com.jhyx.halfroom.dao.UserPlayRecordDao;
import com.jhyx.halfroom.service.UserPlayRecordService;
import com.jhyx.halfroom.vo.app.UserPlayRecordCountVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserPlayRecordServiceImpl implements UserPlayRecordService {
    private UserPlayRecordDao userPlayRecordDao;

    @Override
    public Integer saveUserPlayRecord(UserPlayRecord userPlayRecord) {
        return userPlayRecordDao.insert_into_user_play_record(userPlayRecord);
    }

    @Override
    public UserPlayRecord getUserPlayRecordByUserIdAndBookIdAndChapterIdAndPartId(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        return userPlayRecordDao.select_user_play_record_by_user_id_and_book_id_and_chapter_id_part_id(userId, bookId, bookChapterId, bookChapterPartId);
    }

    @Override
    public Integer updateUserPlayRecord(UserPlayRecord userPlayRecord) {
        return userPlayRecordDao.update_user_play_record(userPlayRecord);
    }

    @Override
    public List<UserPlayRecord> getUserPlayRecordByUserId(Long userId) {
        return userPlayRecordDao.select_user_play_record_by_user_id(userId);
    }

    @Override
    public List<UserPlayRecordCountVo> getUserPlayRecordListByBookIdAndBookChapterId(Integer bookId, Integer bookChapterId) {
        return userPlayRecordDao.select_user_play_record_by_book_id_and_book_chapter_id(bookId, bookChapterId);
    }

    @Override
    public Long getBookChapterPartPlayRecordCount(Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        return userPlayRecordDao.select_user_play_record_play_count_by_book_id_and_chapter_id_part_id(bookId, bookChapterId, bookChapterPartId);
    }
}
