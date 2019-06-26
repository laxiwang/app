package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapterPart;

import java.util.Collection;
import java.util.List;

public interface BookChapterPartService {
    List<BookChapterPart> bookChapterPartFreeList(Integer bookId, Integer offset, Integer limit);

    List<BookChapterPart> getBookChapterPartListByBookIds(Collection<Integer> bookIds);

    Integer getBookChapterPartCountByBookIdAndChapterId(Integer bookId, Integer chapterId);

    List<BookChapterPart> getBookChapterPartListByBookId(Integer bookId);

    BookChapterPart getBookChapterPartByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId);

    List<BookChapterPart> getBookChapterPartListByBookIdAndChapterIdAndType(Integer bookId, Integer chapterId, Integer type, Integer offset, Integer limit);

    List<BookChapterPart> getBookChapterPartListByBookIdAndChapterIdAndState(Integer bookId, Integer chapterId, Integer state, Integer offset, Integer limit);
}
