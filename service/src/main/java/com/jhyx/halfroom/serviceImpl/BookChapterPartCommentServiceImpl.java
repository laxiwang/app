package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapterPartComment;
import com.jhyx.halfroom.dao.BookChapterPartCommentDao;
import com.jhyx.halfroom.service.BookChapterPartCommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BookChapterPartCommentServiceImpl implements BookChapterPartCommentService {
    private BookChapterPartCommentDao bookChapterPartCommentDao;

    @Override
    public Integer saveBookChapterPartComment(BookChapterPartComment comment) {
        return bookChapterPartCommentDao.insert_into_book_chapter_part_comment(comment);
    }

    @Override
    public List<BookChapterPartComment> getCommentIdsByPraise(Long bookId, Integer chapterId, Integer partId, Integer offset, Integer limit) {
        return bookChapterPartCommentDao.select_book_chapter_part_comment_by_praise(bookId, chapterId, partId, offset, limit);
    }

    @Override
    public List<BookChapterPartComment> getCommentsByNotPraise(Long bookId, Integer chapterId, Integer partId, Integer offset, Integer limit) {
        return bookChapterPartCommentDao.select_book_chapter_part_comment_by_book_id_and_chapter_id_and_part_id_and_no_praise(bookId, chapterId, partId, offset, limit);
    }

    @Override
    public Long getCommentCountByBookIdAndChapterIdAndPartId(Integer bookId, Integer bookChapterId, Integer bookChapterPartId) {
        return bookChapterPartCommentDao.select_book_chapter_part_comment_count_by_book_id_and_chapter_id_and_part_id(bookId, bookChapterId, bookChapterPartId);
    }

    @Override
    public List<BookChapterPartComment> getCommentsByHotComment(Long bookId, Integer offset, Integer limit) {
        return bookChapterPartCommentDao.select_book_chapter_part_comment_by_book_id_and_is_hot_comment(bookId, Boolean.TRUE, offset, limit);
    }

    @Override
    public List<BookChapterPartComment> getCommentsByAtCommentIds(Collection<Long> ids) {
        return bookChapterPartCommentDao.select_book_chapter_part_comment_by_at_comment_ids(ids);
    }
}
