package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapterPartComment;

import java.util.Collection;
import java.util.List;

public interface BookChapterPartCommentService {
    Integer saveBookChapterPartComment(BookChapterPartComment comment);

    List<BookChapterPartComment> getCommentIdsByPraise(Long bookId, Integer chapterId, Integer partId, Integer offset, Integer limit);

    List<BookChapterPartComment> getCommentsByNotPraise(Long bookId, Integer chapterId, Integer partId, Integer offset, Integer limit);

    Long getCommentCountByBookIdAndChapterIdAndPartId(Integer bookId, Integer bookChapterId, Integer bookChapterPartId);

    List<BookChapterPartComment> getCommentsByHotComment(Long bookId, Integer offset, Integer limit);

    List<BookChapterPartComment> getCommentsByAtCommentIds(Collection<Long> ids);
}
