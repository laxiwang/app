package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapterPart;
import com.jhyx.halfroom.constant.BookChapterPartStatus;
import com.jhyx.halfroom.dao.BookChapterPartDao;
import com.jhyx.halfroom.service.BookChapterPartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BookChapterPartServiceImpl implements BookChapterPartService {
    private BookChapterPartDao bookChapterPartDao;

    @Override
    public List<BookChapterPart> bookChapterPartFreeList(Integer bookId, Integer offset, Integer limit) {
        return bookChapterPartDao.select_book_chapter_part_by_state_and_book_id_and_chapter_id(bookId, null, BookChapterPartStatus.FREE.getIndex(), offset, limit);
    }

    @Override
    public List<BookChapterPart> getBookChapterPartListByBookIds(Collection<Integer> bookIds) {
        return bookChapterPartDao.select_book_chapter_part_by_book_id(bookIds);
    }

    @Override
    public Integer getBookChapterPartCountByBookIdAndChapterId(Integer bookId, Integer chapterId) {
        return bookChapterPartDao.select_book_chapter_part_by_book_id_and_chapter_id(bookId, chapterId);
    }


    @Override
    public List<BookChapterPart> getBookChapterPartListByBookId(Integer bookId) {
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(bookId);
        return bookChapterPartDao.select_book_chapter_part_by_book_id(bookIds);
    }

    @Override
    public BookChapterPart getBookChapterPartByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId) {
        return bookChapterPartDao.select_book_chapter_part_by_book_id_and_book_chapter_id_book_chapter_part(bookId, chapterId, partId);
    }

    @Override
    public List<BookChapterPart> getBookChapterPartListByBookIdAndChapterIdAndType(Integer bookId, Integer chapterId, Integer type, Integer offset, Integer limit) {
        return bookChapterPartDao.select_book_chapter_part_by_book_id_and_book_chapter_id_and_type(bookId, chapterId, type, offset, limit);
    }

    @Override
    public List<BookChapterPart> getBookChapterPartListByBookIdAndChapterIdAndState(Integer bookId, Integer chapterId, Integer state, Integer offset, Integer limit) {
        return bookChapterPartDao.select_book_chapter_part_by_state_and_book_id_and_chapter_id(bookId, chapterId, state, offset, limit);
    }
}
