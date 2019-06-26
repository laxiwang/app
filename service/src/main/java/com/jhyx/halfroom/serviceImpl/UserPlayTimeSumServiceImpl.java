package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserPlayTimeSum;
import com.jhyx.halfroom.dao.UserPlayTimeSumDao;
import com.jhyx.halfroom.service.UserPlayTimeSumService;
import com.jhyx.halfroom.vo.app.UserPlayTimeSumVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserPlayTimeSumServiceImpl implements UserPlayTimeSumService {
    private UserPlayTimeSumDao userPlayTimeSumDao;

    @Override
    public List<UserPlayTimeSum> getUserPlayTimeSumByUserIdAndBookIdAndBookChapterIdAndBookChapterPartId(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        return userPlayTimeSumDao.select_user_play_time_sum_by_user_id_and_book_id_and_book_chapter_id_and_book_chapter_part_id(userId, bookId, bookChapterId, bookChapterPartId);
    }

    @Override
    public void saveUserPlayTimeSum(UserPlayTimeSum userPlayTimeSum) {
        userPlayTimeSumDao.insert_into_user_play_time_sum(userPlayTimeSum);
    }

    @Override
    public void updateUserPlayTimeSum(UserPlayTimeSum userPlayTimeSum) {
        userPlayTimeSumDao.update_user_play_time_sum(userPlayTimeSum);
    }

    @Override
    public List<UserPlayTimeSumVo> getUserPlayTimeSum() {
        return userPlayTimeSumDao.select_user_play_time_sum();
    }
}
