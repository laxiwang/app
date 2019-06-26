package com.jhyx.halfroom.wap.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.commons.PhoneUtil;
import com.jhyx.halfroom.commons.UniqueKeyGeneratorUtil;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v4/wap/card")
@AllArgsConstructor
@Slf4j
public class WapCardController {
    private CardService cardService;
    private CommonService commonService;
    private UserService userService;
    private UserOrderService userOrderService;
    private UserBookService userBookService;
    private BookService bookService;
    private CardUsedRecordService cardUsedRecordService;

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Integer> getCardStatus(@RequestParam Integer cardNo) {
        try {
            //默认不存在
            Integer status = -1;
            Card card = cardService.getCardByCardNo(cardNo);
            if (card != null)
                status = card.getStatus();
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), status);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/open/experience/card", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> openExperienceCard(@RequestParam String nickName,
                                              @RequestParam String phone,
                                              @RequestParam Integer cardNo,
                                              @RequestParam String cardCode) {
        try {
            Card card = cardService.getCardByCardNoAndCardCodeAndType(cardNo, cardCode, CardTypeStatus.EXPERIENCE_CARD.getIndex());
            Result<Boolean> result = judgeCardInfo(card);
            if (result != null)
                return result;
            User user = userService.getUserByPhone(phone);
            if (user == null) {
                Map<String, String> map = PhoneUtil.getProvinceAndCityByPhone(phone);
                if (StringUtils.isEmpty(map.get("province")) || StringUtils.isEmpty(map.get("city")))
                    return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
                user = commonService.createUser(phone, map.get("province"), map.get("city"), null, "card", UniqueKeyGeneratorUtil.generateToken());
                user.setBranchsaler(card.getBranchsalerId());
            }
            user.setName(nickName);
            userService.updateUser(user);
            List<CardUsedRecord> list = cardUsedRecordService.getCardUsedRecordByUserIdAndCardType(user.getId(), CardTypeStatus.EXPERIENCE_CARD.getIndex());
            if (list.size() > 0)
                return new Result<>(ResultCode.HAS_USED_EXPERIENCE_CARD.getIndex(), ResultCode.HAS_USED_EXPERIENCE_CARD.getMsg(), Boolean.FALSE);
            List<UserBook> userBookList = userBookService.getUserBookListByUserId(user.getId());
            List<Book> bookList = bookService.getAllBookList();
            if (userBookList.size() == 0) {
                LocalDateTime now = LocalDateTime.now();
                for (Book book : bookList) {
                    UserBook userBook = new UserBook();
                    userBook.setBookId(book.getId()).setUserId(user.getId()).setType(UserBookTypeStatus.EXP.getIndex());
                    userBook.setStartTime(LocalDateTimeUtil.formatLocalDateTime(now)).setEndTime(LocalDateTimeUtil.formatLocalDateTime(now.plusDays(15)));
                    userBookList.add(userBook);
                }
                userBookService.batchSaveUserBook(userBookList);
            } else {
                for (Book book : bookList) {
                    boolean flag = false;
                    for (UserBook userBook : userBookList) {
                        if (book.getId().equals(userBook.getBookId())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        UserBook userBook = new UserBook();
                        userBook.setBookId(book.getId()).setUserId(user.getId()).setType(UserBookTypeStatus.EXP.getIndex());
                        LocalDateTime startTime = LocalDateTimeUtil.parseLocalDateTime(userBookList.get(0).getStartTime());
                        if (startTime == null) {
                            startTime = LocalDateTime.now();
                        }
                        userBook.setEndTime(LocalDateTimeUtil.formatLocalDateTime(startTime.plusDays(7))).setStartTime(LocalDateTimeUtil.formatLocalDateTime(startTime));
                        userBookService.saveUserBook(userBook);
                        userBookList.add(userBook);
                    }
                }
                for (UserBook userBook : userBookList) {
                    LocalDateTime endTime = LocalDateTimeUtil.parseLocalDateTime(userBook.getEndTime());
                    if (endTime != null && endTime.isAfter(LocalDateTime.now())) {
                        userBook.setEndTime(LocalDateTimeUtil.formatLocalDateTime(endTime.plusDays(15)));
                    } else {
                        userBook.setEndTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now().plusDays(15)));
                    }
                    userBookService.updateUserBook(userBook);
                }
            }
            updateCardStatusAndCreateCardUsedRecord(card, user.getId());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
    }

    @PostMapping(value = "/open/card", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> openCard(@RequestParam String nickName,
                                    @RequestParam String phone,
                                    @RequestParam Integer cardNo,
                                    @RequestParam String cardCode,
                                    @RequestParam Integer bookId) {
        try {
            Card card = cardService.getCardByCardNoAndCardCodeAndType(cardNo, cardCode, CardTypeStatus.OFFICIAL_CARD.getIndex());
            Result<Boolean> result = judgeCardInfo(card);
            if (result != null)
                return result;
            Book book = bookService.getBookById(bookId);
            if (book == null || book.getPrice() == null || card.getFee() == null || !(card.getFee().equals(book.getPrice()))) {
                return new Result<>(ResultCode.CARD_FEE_NOT_MATCH_BOOK_PRICE.getIndex(), ResultCode.CARD_FEE_NOT_MATCH_BOOK_PRICE.getMsg(), null);
            }
            User user = userService.getUserByPhone(phone);
            if (user != null) {
                if (commonService.judgeUserWhetherPurchaseBookByUserIdAndBookId(user.getId(), bookId)) {
                    return new Result<>(ResultCode.USER_HAS_PURCHASE_BOOK.getIndex(), ResultCode.USER_HAS_PURCHASE_BOOK.getMsg(), null);
                }
            }
            if (user == null) {
                Map<String, String> map = PhoneUtil.getProvinceAndCityByPhone(phone);
                String province = map.get("province");
                String city = map.get("city");
                if (StringUtils.isEmpty(province) || StringUtils.isEmpty(city))
                    return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
                user = commonService.createUser(phone, province, city, null, "card", UniqueKeyGeneratorUtil.generateToken());
            }
            user.setName(nickName);
            if (card.getFee().compareTo(new BigDecimal("98")) > 0) {
                Integer userBranchSaleId = user.getBranchsaler();
                Integer cardBranchSaleId = card.getBranchsalerId();
                commonService.saveUserBranchSaleChange(user.getId(), userBranchSaleId, cardBranchSaleId);
                user.setBranchsaler(cardBranchSaleId);
            }
            userService.updateUser(user);
            UserOrder userOrder = commonService.createUserOrder(user.getId(), bookId, card.getFee(), "card", null, UserOrderPayRoleStatus.NATURE_ROLE);
            userOrder.setOrderno(card.getCardCode()).setState(UserOrderStatus.COMPLETE_PAY.getIndex()).setUpdatetime(LocalDateTimeUtil.getCurrentTimestamp());
            userOrderService.saveUserOrder(userOrder);
            commonService.openCourseVipPermission(user.getId(), bookId);
            updateCardStatusAndCreateCardUsedRecord(card, user.getId());
            //在这里写发送短信通知吧try-catch
            try {
                commonService.sendPayNotice(userOrder, user, book);
            } catch (Exception e) {
                log.error(e.getMessage(), e.getCause());
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
    }

    private Result<Boolean> judgeCardInfo(Card card) {
        if (card == null)
            return new Result<>(ResultCode.CARD_NOT_EXITS.getIndex(), ResultCode.CARD_NOT_EXITS.getMsg(), Boolean.FALSE);
        if (card.getStatus().equals(CardStatus.NO_ACTIVATE.getIndex()))
            return new Result<>(ResultCode.CARD_NOT_ACTIVATE.getIndex(), ResultCode.CARD_NOT_ACTIVATE.getMsg(), Boolean.FALSE);
        if (card.getStatus().equals(CardStatus.HAS_USED.getIndex()))
            return new Result<>(ResultCode.CARD_HAS_USED.getIndex(), ResultCode.CARD_HAS_USED.getMsg(), Boolean.FALSE);
        return null;
    }

    private void updateCardStatusAndCreateCardUsedRecord(Card card, Long userId) {
        card.setStatus(CardStatus.HAS_USED.getIndex()).setUpdatetime(LocalDateTimeUtil.getCurrentTimestamp());
        cardService.updateCard(card);
        CardUsedRecord cardUsedRecord = new CardUsedRecord();
        cardUsedRecord.setCardNo(card.getCardNo().longValue()).setCardType(card.getType()).setUserId(userId);
        cardUsedRecordService.saveCardUsedRecord(cardUsedRecord);
    }
}
