package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapterPartCommentPraise;
import com.jhyx.halfroom.dao.BookChapterPartCommentPraiseDao;
import com.jhyx.halfroom.service.BookChapterPartCommentPraiseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookChapterPartCommentPraiseServiceImpl implements BookChapterPartCommentPraiseService {
    private BookChapterPartCommentPraiseDao bookChapterPartCommentPraiseDao;

    @Override
    public BookChapterPartCommentPraise getBookChapterPartCommentPraiseByCommentIdAndUserId(Long commentId, Long userId) {
        return bookChapterPartCommentPraiseDao.select_book_chapter_part_comment_praise_by_user_id_and_comment_id(userId, commentId);
    }

    @Override
    public Integer saveBookChapterPartCommentPraise(BookChapterPartCommentPraise commentPraise) {
        return bookChapterPartCommentPraiseDao.insert_into_book_chapter_part_comment_praise(commentPraise);
    }

    @Override
    public Integer deleteBookChapterPartCommentPraiseByCommentIdAndUserId(Long commentId, Long userId) {
        return bookChapterPartCommentPraiseDao.delete_book_chapter_part_comment_praise_by_comment_id_and_user_id(commentId, userId);
    }

    @Override
    public Integer getBookChapterPartCommentPraiseCount(Long commentId) {
        return bookChapterPartCommentPraiseDao.select_book_chapter_part_comment_praise_count_by_comment_id(commentId);
    }
}
