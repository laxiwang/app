package com.jhyx.halfroom.service;

import com.jhyx.halfroom.bean.UserBook;

import java.util.Collection;
import java.util.List;

public interface UserBookService {
    Integer batchSaveUserBook(Collection<UserBook> userBooks);

    UserBook getUserBookListByUserIdAndBookId(Long userId, Integer bookId);

    List<UserBook> getUserBookListByUserId(Long userId);

    List<UserBook> getUserPurchasedBookList(Long userId);

    Integer updateUserBook(UserBook userBook);

    Integer saveUserBook(UserBook userBook);
}
