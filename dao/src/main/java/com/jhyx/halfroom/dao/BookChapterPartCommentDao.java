package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapterPartComment;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface BookChapterPartCommentDao {
    Integer insert_into_book_chapter_part_comment(BookChapterPartComment comment);

    List<BookChapterPartComment> select_book_chapter_part_comment_by_praise(@Param("bookId") Long bookId,
                                                                            @Param("chapterId") Integer chapterId,
                                                                            @Param("partId") Integer partId,
                                                                            @Param("offset") Integer offset,
                                                                            @Param("limit") Integer limit);

    List<BookChapterPartComment> select_book_chapter_part_comment_by_book_id_and_chapter_id_and_part_id_and_no_praise(@Param("bookId") Long bookId,
                                                                                                                      @Param("chapterId") Integer chapterId,
                                                                                                                      @Param("partId") Integer partId,
                                                                                                                      @Param("offset") Integer offset,
                                                                                                                      @Param("limit") Integer limit);

    Long select_book_chapter_part_comment_count_by_book_id_and_chapter_id_and_part_id(@Param("bookId") Integer bookId,
                                                                                      @Param("chapterId") Integer bookChapterId,
                                                                                      @Param("partId") Integer bookChapterPartId);

    List<BookChapterPartComment> select_book_chapter_part_comment_by_book_id_and_is_hot_comment(@Param("bookId") Long bookId,
                                                                                                @Param("isHotComment") Boolean isHotComment,
                                                                                                @Param("offset") Integer offset,
                                                                                                @Param("limit") Integer limit);

    List<BookChapterPartComment> select_book_chapter_part_comment_by_at_comment_ids(@Param("ids") Collection<Long> ids);
}
