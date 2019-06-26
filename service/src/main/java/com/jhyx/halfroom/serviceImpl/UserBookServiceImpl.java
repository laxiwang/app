package com.jhyx.halfroom.serviceImpl;

import com.jhyx.halfroom.bean.UserBook;
import com.jhyx.halfroom.constant.UserBookStatus;
import com.jhyx.halfroom.dao.UserBookDao;
import com.jhyx.halfroom.service.UserBookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserBookServiceImpl implements UserBookService {
    private UserBookDao userBookDao;

    @Override
    public Integer batchSaveUserBook(Collection<UserBook> userBooks) {
        return userBookDao.batch_insert_into_user_book(userBooks);
    }

    @Override
    public Integer saveUserBook(UserBook userBook) {
        return userBookDao.insert_into_user_book(userBook);
    }

    @Override
    public UserBook getUserBookListByUserIdAndBookId(Long userId, Integer bookId) {
        return userBookDao.select_user_book_by_user_id_and_book_id(userId, bookId);
    }

    @Override
    public List<UserBook> getUserBookListByUserId(Long userId) {
        return userBookDao.select_user_book_by_user_id(userId);
    }

    @Override
    public List<UserBook> getUserPurchasedBookList(Long userId) {
        return userBookDao.select_user_book_by_user_id_and_type(userId, UserBookStatus.VIP.getIndex());
    }

    @Override
    public Integer updateUserBook(UserBook userBook) {
        return userBookDao.update_user_book(userBook);
    }
}
