package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.Book;
import com.jhyx.halfroom.dao.BookDao;
import com.jhyx.halfroom.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private BookDao bookDao;

    @Override
    public List<Book> getBookListByCategory(Integer category, Integer offset, Integer limit) {
        return bookDao.select_book_by_category(category, offset, limit);
    }

    @Override
    public List<Book> getAllBookList() {
        return bookDao.select_book();
    }

    @Override
    public Book getBookById(Integer bookId) {
        return bookDao.select_book_by_id(bookId);
    }

    @Override
    public List<Book> getBookByIds(Collection<Integer> bookIds) {
        return bookDao.select_book_by_ids(bookIds);
    }
}
