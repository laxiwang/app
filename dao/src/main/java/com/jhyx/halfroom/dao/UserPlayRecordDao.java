package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserPlayRecord;
import com.jhyx.halfroom.vo.app.UserPlayRecordCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPlayRecordDao {
    Integer insert_into_user_play_record(UserPlayRecord userPlayRecord);

    UserPlayRecord select_user_play_record_by_user_id_and_book_id_and_chapter_id_part_id(@Param("userId") Long userId,
                                                                                         @Param("bookId") Integer bookId,
                                                                                         @Param("bookChapterId") Integer bookChapterId,
                                                                                         @Param("bookChapterPartId") Integer bookChapterPartId);

    Integer update_user_play_record(UserPlayRecord userPlayRecord);

    List<UserPlayRecord> select_user_play_record_by_user_id(@Param("userId") Long userId);

    List<UserPlayRecordCountVo> select_user_play_record_by_book_id_and_book_chapter_id(@Param("bookId") Integer bookId,
                                                                                       @Param("bookChapterId") Integer bookChapterId);

    Long select_user_play_record_play_count_by_book_id_and_chapter_id_part_id(@Param("bookId") Integer bookId,
                                                                              @Param("bookChapterId") Integer bookChapterId,
                                                                              @Param("bookChapterPartId") Integer bookChapterPartId);
}
