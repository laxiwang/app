package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserPlayTimeSum;
import com.jhyx.halfroom.vo.app.UserPlayTimeSumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPlayTimeSumDao {
    List<UserPlayTimeSum> select_user_play_time_sum_by_user_id_and_book_id_and_book_chapter_id_and_book_chapter_part_id(@Param("userId") Long userId,
                                                                                                                        @Param("bookId") Integer bookId,
                                                                                                                        @Param("bookChapterId") Integer bookChapterId,
                                                                                                                        @Param("bookChapterPartId") Integer bookChapterPartId);

    void insert_into_user_play_time_sum(UserPlayTimeSum userPlayTimeSum);

    void update_user_play_time_sum(UserPlayTimeSum userPlayTimeSum);

    List<UserPlayTimeSumVo> select_user_play_time_sum();
}
