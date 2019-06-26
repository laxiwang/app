package com.jhyx.halfroom.dao;

import com.jhyx.halfroom.bean.UserBook;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserBookDao {
    Integer batch_insert_into_user_book(@Param("userBooks") Collection<UserBook> userBooks);

    UserBook select_user_book_by_user_id_and_book_id(@Param("userId") Long userId, @Param("bookId") Integer bookId);

    List<UserBook> select_user_book_by_user_id_and_type(@Param("userId") Long userId, @Param("type") Integer type);

    Integer update_user_book(UserBook userBook);

    List<UserBook> select_user_book_by_user_id(@Param("userId") Long userId);

    Integer insert_into_user_book(UserBook userBook);
}
