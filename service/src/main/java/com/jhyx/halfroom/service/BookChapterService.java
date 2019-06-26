package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapter;

import java.util.Collection;
import java.util.List;

public interface BookChapterService {
    List<BookChapter> getBookChapterListByBookIds(Collection<Integer> bookIds);

    List<BookChapter> getBookChapterListByBookId(Integer bookId);
}
