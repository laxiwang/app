package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapterPartCommentPraise;

public interface BookChapterPartCommentPraiseService {
    BookChapterPartCommentPraise getBookChapterPartCommentPraiseByCommentIdAndUserId(Long commentId, Long userId);

    Integer saveBookChapterPartCommentPraise(BookChapterPartCommentPraise commentPraise);

    Integer deleteBookChapterPartCommentPraiseByCommentIdAndUserId(Long commentId, Long userId);

    Integer getBookChapterPartCommentPraiseCount(Long commentId);
}
