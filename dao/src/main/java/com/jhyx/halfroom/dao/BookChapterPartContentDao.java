package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapterPartContent;
import org.apache.ibatis.annotations.Param;

public interface BookChapterPartContentDao {
    BookChapterPartContent select_book_chapter_part_content_by_part_id(@Param("partId") Integer partId);

    BookChapterPartContent select_book_chapter_part_content_by_book_id_and_chapter_id_and_part_id(@Param("bookId") Integer bookId,
                                                                                                  @Param("bookChapterId") Integer bookChapterId,
                                                                                                  @Param("bookChapterPartId") Integer bookChapterPartId);
}
