package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapterPartCommentPraise;
import org.apache.ibatis.annotations.Param;

public interface BookChapterPartCommentPraiseDao {
    BookChapterPartCommentPraise select_book_chapter_part_comment_praise_by_user_id_and_comment_id(@Param("userId") Long userId,
                                                                                                   @Param("commentId") Long commentId);

    Integer insert_into_book_chapter_part_comment_praise(BookChapterPartCommentPraise commentPraise);

    Integer delete_book_chapter_part_comment_praise_by_comment_id_and_user_id(@Param("commentId") Long commentId,
                                                                              @Param("userId") Long userId);

    Integer select_book_chapter_part_comment_praise_count_by_comment_id(@Param("commentId") Long commentId);
}
