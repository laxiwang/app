package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.BookChapter;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface BookChapterDao {
    List<BookChapter> select_book_chapter_by_book_ids(@Param("bookIds") Collection<Integer> bookIds);
}
