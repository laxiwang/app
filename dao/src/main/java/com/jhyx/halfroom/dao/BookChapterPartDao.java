package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapterPart;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface BookChapterPartDao {
    List<BookChapterPart> select_book_chapter_part_by_state_and_book_id_and_chapter_id(@Param("bookId") Integer bookId,
                                                            @Param("chapterId") Integer chapterId,
                                                            @Param("state") Integer state,
                                                            @Param("offset") Integer offset,
                                                            @Param("limit") Integer limit);

    List<BookChapterPart> select_book_chapter_part_by_book_id(@Param("ids") Collection<Integer> bookIds);

    Integer select_book_chapter_part_by_book_id_and_chapter_id(@Param("bookId") Integer bookId,
                                                               @Param("chapterId") Integer chapterId);


    BookChapterPart select_book_chapter_part_by_book_id_and_book_chapter_id_book_chapter_part(@Param("bookId") Integer bookId,
                                                                                              @Param("chapterId") Integer chapterId,
                                                                                              @Param("partId") Integer partId);

    List<BookChapterPart> select_book_chapter_part_by_book_id_and_book_chapter_id_and_type(@Param("bookId") Integer bookId,
                                                                                           @Param("chapterId") Integer chapterId,
                                                                                           @Param("type") Integer type,
                                                                                           @Param("offset") Integer offset,
                                                                                           @Param("limit") Integer limit);
}
