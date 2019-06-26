package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapterPartArchitecture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookChapterPartArchitectureDao {
    List<BookChapterPartArchitecture> select_book_chapter_part_architecture_by_book_id_and_book_chapter_book_chapter_part_id(@Param("bookId") Integer bookId,
                                                                                                                             @Param("chapterId") Integer chapterId,
                                                                                                                             @Param("partId") Integer partId);
}
