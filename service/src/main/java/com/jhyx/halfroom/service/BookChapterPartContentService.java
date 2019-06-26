package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapterPartContent;

public interface BookChapterPartContentService {
    BookChapterPartContent getBookChapterPartContentByPartId(Integer partId);

    BookChapterPartContent getBookChapterPartContentByBookIdAndChapterIdAndPartId(Integer bookId, Integer bookChapterId, Integer bookChapterPartId);
}
