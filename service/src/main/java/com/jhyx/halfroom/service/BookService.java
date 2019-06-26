package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.Book;

import java.util.Collection;
import java.util.List;

public interface BookService {
    List<Book> getBookListByCategory(Integer category, Integer offset, Integer limit);

    Book getBookById(Integer bookId);

    List<Book> getAllBookList();

    List<Book> getBookByIds(Collection<Integer> bookIds);
}
