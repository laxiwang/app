package com.jhyx.halfroom.wap.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import com.jhyx.halfroom.vo.wap.BookVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v4/wap/user")
@RequiredArgsConstructor
@Slf4j
public class WapUserController {
    private final UserLoginService userLoginService;
    private final CommonService commonService;
    private final UserBookService userBookService;
    private final BookService bookService;
    private final BranchInviteService branchInviteService;
    private final UserService userService;
    private final UserManualService userManualService;

    @GetMapping(value = "/check/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> checkToken(@RequestParam String token) {
        try {
            Long userId = userLoginService.getUserLoginByToken(token);
            if (userId == null)
                return new Result<>(ResultCode.USER_NOT_LOGIN.getIndex(), ResultCode.USER_NOT_LOGIN.getMsg(), Boolean.FALSE);
            else
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/purchased/book", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BookVo>> userPurchasedBook(HttpServletRequest request) {
        List<BookVo> results = new ArrayList<>();
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            List<UserBook> list = userBookService.getUserPurchasedBookList(userId);
            if (list != null && list.size() > 0) {
                Set<Integer> bookIds = list.stream().map(UserBook::getBookId).collect(Collectors.toSet());
                List<Book> books = bookService.getBookByIds(bookIds);
                for (UserBook userBook : list) {
                    BookVo vo = new BookVo();
                    Optional<Book> optional = books.stream().filter(book -> book.getId().equals(userBook.getBookId())).findFirst();
                    optional.ifPresent(book -> {
                        vo.setBookId(book.getId()).setSpeaker(book.getSpeaker()).setAgeMax(book.getAgeMax()).setAgeMin(book.getAgeMin()).setName(book.getName()).setFaceThumbUrl(book.getSmallFaceThumbUrl());
                        vo.setCourseCount(book.getCourseCount()).setType(UserPurchaseBookStatus.PAY.getIndex());
                    });
                    results.add(vo);
                }
            }
            List<Manual> manualList = userManualService.getManualListByUserId(userId);
            if (manualList != null && manualList.size() > 0) {
                for (Manual manual : manualList) {
                    BookVo vo = new BookVo();
                    vo.setUrl(manual.getUrl()).setName(manual.getName()).setSpeaker(manual.getSpeaker()).setPage(manual.getPage()).setFaceThumbUrl(manual.getFaceThumbUrl()).setType(UserPurchaseBookStatus.PRESENTATION.getIndex());
                    results.add(vo);
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/accept/invite", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> userAcceptInvite(@RequestParam Long userId, @RequestParam Integer branchId) {
        try {
            BranchInvite source = null;
            List<BranchInvite> list = branchInviteService.getBranchInviteListByUserIdAndStatus(userId, BranchInviteStatus.WAIT_INVITE);
            if (list != null && list.size() > 0) {
                source = list.get(0);
            }
            if (source == null || !source.getBranchsalerId().equals(branchId))
                return new Result<>(ResultCode.BRANCH_INVITE_RECORD_NOT_EXITS.getIndex(), ResultCode.BRANCH_INVITE_RECORD_NOT_EXITS.getMsg(), null);
            source.setStatus(BranchInviteStatus.ACCEPT_INVITE.getIndex()).setUpdatetime(LocalDateTimeUtil.getCurrentTimestamp());
            branchInviteService.updateBranchInvite(source);
            User user = userService.getUserById(userId);
            user.setRole(UserRoleStatus.SALE_AMBASSADOR.getIndex());
            userService.updateUser(user);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
