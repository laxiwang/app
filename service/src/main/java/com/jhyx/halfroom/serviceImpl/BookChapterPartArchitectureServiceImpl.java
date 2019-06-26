package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.BookChapterPartArchitecture;
import com.jhyx.halfroom.dao.BookChapterPartArchitectureDao;
import com.jhyx.halfroom.service.BookChapterPartArchitectureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookChapterPartArchitectureServiceImpl implements BookChapterPartArchitectureService {
    private BookChapterPartArchitectureDao bookChapterPartArchitectureDao;

    @Override
    public List<BookChapterPartArchitecture> getBookChapterPartArchitectureListByBookIdAndChapterIdAndPartId(Integer bookId, Integer chapterId, Integer partId) {
        return bookChapterPartArchitectureDao.select_book_chapter_part_architecture_by_book_id_and_book_chapter_book_chapter_part_id(bookId, chapterId, partId);
    }
}
