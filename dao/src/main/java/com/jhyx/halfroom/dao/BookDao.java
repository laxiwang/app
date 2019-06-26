package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.Book;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface BookDao {
    List<Book> select_book_by_category(@Param("category") Integer category,
                                       @Param("offset") Integer offset,
                                       @Param("limit") Integer limit);

    List<Book> select_book();

    Book select_book_by_id(@Param("bookId") Integer bookId);

    List<Book> select_book_by_ids(@Param("bookIds") Collection<Integer> bookIds);
}
