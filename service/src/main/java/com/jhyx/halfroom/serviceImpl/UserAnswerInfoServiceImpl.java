package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserAnswerInfo;
import com.jhyx.halfroom.dao.UserAnswerInfoDao;
import com.jhyx.halfroom.service.UserAnswerInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserAnswerInfoServiceImpl implements UserAnswerInfoService {
    private UserAnswerInfoDao userAnswerInfoDao;

    @Override
    public String userAnswerInfo(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        String result = "未答题";
        if (userId != null && bookId != null && bookChapterId != null && bookChapterPartId != null) {
            List<UserAnswerInfo> list = userAnswerInfoDao.select_user_book_chapter_part_architecture_answer_by_user_id_and_book_id_and_book_chapter_id_and_book_chapter_part_id(userId, bookId, bookChapterId, bookChapterPartId);
            if (list != null && list.size() > 0) {
                UserAnswerInfo userAnswerInfo = list.get(0);
                if (userAnswerInfo != null && userAnswerInfo.getCorrectCount() != null) {
                    result = "答对" + userAnswerInfo.getCorrectCount() + "/3";
                }
            }
        }
        return result;
    }

    @Override
    public List<UserAnswerInfo> userAnswerInfoList(Long userId, Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        List<UserAnswerInfo> result = new ArrayList<>();
        if (userId != null && bookId != null) {
            result = userAnswerInfoDao.select_user_book_chapter_part_architecture_answer_by_user_id_and_book_id_and_book_chapter_id_and_book_chapter_part_id(userId, bookId, bookChapterId, bookChapterPartId);
        }
        return result;
    }

    @Override
    public void updateUserAnswerInfo(UserAnswerInfo userAnswerInfo) {
        userAnswerInfoDao.update_user_book_chapter_part_architecture_answer(userAnswerInfo);
    }

    @Override
    public void saveUserAnswerInfo(UserAnswerInfo userAnswerInfo) {
        userAnswerInfoDao.insert_into_user_book_chapter_part_architecture_answer(userAnswerInfo);
    }
}
