package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapter;
import com.jhyx.halfroom.dao.BookChapterDao;
import com.jhyx.halfroom.service.BookChapterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BookChapterServiceImpl implements BookChapterService {
    private BookChapterDao bookChapterDao;

    @Override
    public List<BookChapter> getBookChapterListByBookIds(Collection<Integer> bookIds) {
        return bookChapterDao.select_book_chapter_by_book_ids(bookIds);
    }

    @Override
    public List<BookChapter> getBookChapterListByBookId(Integer bookId) {
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(bookId);
        return bookChapterDao.select_book_chapter_by_book_ids(bookIds);
    }
}
