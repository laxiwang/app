package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.BookChapterPartArchitecture;

import java.util.List;

public interface BookChapterPartArchitectureService {
    List<BookChapterPartArchitecture> getBookChapterPartArchitectureListByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId);
}
