package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserAnswerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAnswerInfoDao {
    List<UserAnswerInfo> select_user_book_chapter_part_architecture_answer_by_user_id_and_book_id_and_book_chapter_id_and_book_chapter_part_id(@Param("userId") Long userId,
                                                                                                                                               @Param("bookId") Integer bookId,
                                                                                                                                               @Param("bookChapterId") Integer bookChapterId,
                                                                                                                                               @Param("bookChapterPartId") Integer bookChapterPartId);

    void update_user_book_chapter_part_architecture_answer(UserAnswerInfo userAnswerInfo);

    void insert_into_user_book_chapter_part_architecture_answer(UserAnswerInfo userAnswerInfo);
}
