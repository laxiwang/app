package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapterPartContent;
import com.jhyx.halfroom.dao.BookChapterPartContentDao;
import com.jhyx.halfroom.service.BookChapterPartContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookChapterPartContentServiceImpl implements BookChapterPartContentService {
    private BookChapterPartContentDao bookChapterPartContentDao;

    @Override
    public BookChapterPartContent getBookChapterPartContentByPartId(Integer partId) {
        return bookChapterPartContentDao.select_book_chapter_part_content_by_part_id(partId);
    }

    @Override
    public BookChapterPartContent getBookChapterPartContentByBookIdAndChapterIdAndPartId(Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        return bookChapterPartContentDao.select_book_chapter_part_content_by_book_id_and_chapter_id_and_part_id(bookId, bookChapterId, bookChapterPartId);
    }
}
