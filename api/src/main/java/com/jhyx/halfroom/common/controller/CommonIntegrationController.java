package com.jhyx.halfroom.common.controller;

import com.jhyx.halfroom.bean.Book;
import com.jhyx.halfroom.bean.IntegralExchangeRecord;
import com.jhyx.halfroom.bean.IntegralExchangeRule;
import com.jhyx.halfroom.bean.IntegralOrigin;
import com.jhyx.halfroom.commons.LocalDateTimeUtil;
import com.jhyx.halfroom.constant.IntegrationTypeStatus;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.BookService;
import com.jhyx.halfroom.service.CommonService;
import com.jhyx.halfroom.service.IntegralService;
import com.jhyx.halfroom.vo.app.ExchangeIntegrationGoodsVo;
import com.jhyx.halfroom.vo.app.IntegrationDetailVo;
import com.jhyx.halfroom.vo.app.IntegrationGoodsVo;
import com.jhyx.halfroom.vo.app.IntegrationOriginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v4/common/integration")
@RequiredArgsConstructor
@Slf4j
public class CommonIntegrationController {
    private final IntegralService integralService;
    private final BookService bookService;
    private final CommonService commonService;

    @GetMapping(value = "/goods/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<IntegrationGoodsVo>> integrationGoodsList(HttpServletRequest request) {
        List<IntegrationGoodsVo> results = new ArrayList<>();
        try {
            List<IntegralExchangeRule> rules = integralService.getIntegralExchangeRuleList();
            for (IntegralExchangeRule rule : rules) {
                IntegrationGoodsVo vo = new IntegrationGoodsVo();
                Book book = bookService.getBookById(rule.getBookId());
                vo.setName(book.getName()).setFaceThumbUrl(book.getSmallFaceThumbUrl()).setGrade(book.getGrade());
                vo.setIntegration(rule.getIntegral()).setExchangeCount(integralService.getIntegralGoodsExchangeCount(book.getId())).setBookId(book.getId());
                Long userId = commonService.getUserIdNoNeedLogin(request);
                Integer integration = integralService.getUserIntegral(userId);
                vo.setFlag(rule.getIntegral() <= integration);
                vo.setAgeMax(book.getAgeMax()).setAgeMin(book.getAgeMin());
                results.add(vo);
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/exchange/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> exchangeGoods(@RequestParam Integer bookId, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (commonService.judgeUserWhetherPurchaseBookByUserIdAndBookId(userId, bookId))
                return new Result<>(ResultCode.USER_HAS_PURCHASE_BOOK.getIndex(), ResultCode.USER_HAS_PURCHASE_BOOK.getMsg(), null);
            Integer integration = integralService.getUserIntegral(userId);
            IntegralExchangeRule rule = integralService.getIntegralExchangeRuleByBookId(bookId);
            if (rule.getIntegral() > integration)
                return new Result<>(ResultCode.INTEGRATION_DEFICIENCY.getIndex(), ResultCode.INTEGRATION_DEFICIENCY.getMsg(), Boolean.FALSE);
            IntegralExchangeRecord exchange = new IntegralExchangeRecord();
            exchange.setBookId(bookId).setUserId(userId).setIntegral(rule.getIntegral());
            integralService.saveIntegralExchange(exchange);
            commonService.openCourseVipPermission(userId, bookId);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
    }

    @GetMapping(value = "/exchange/history", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<ExchangeIntegrationGoodsVo>> exchangeGoodsHistory(HttpServletRequest request) {
        List<ExchangeIntegrationGoodsVo> results = new ArrayList<>();
        try {
            Long userId = (Long) request.getAttribute("userId");
            List<IntegralExchangeRecord> list = integralService.getIntegralExchangeListByUserId(userId);
            if (list != null && list.size() > 0) {
                for (IntegralExchangeRecord exchange : list) {
                    ExchangeIntegrationGoodsVo vo = new ExchangeIntegrationGoodsVo();
                    vo.setCreateTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(exchange.getCreateTime()))).setIntegration(exchange.getIntegral());
                    Book book = bookService.getBookById(exchange.getBookId());
                    vo.setName(book.getName()).setFaceThumbUrl(book.getSmallFaceThumbUrl());
                    results.add(vo);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<IntegrationDetailVo> integrationDetails(HttpServletRequest request) {
        IntegrationDetailVo result = new IntegrationDetailVo();
        try {
            Long userId = (Long) request.getAttribute("userId");
            result.setIntegrationSum(integralService.getUserIntegral(userId));
            List<IntegrationOriginVo> integrationOrigin = new ArrayList<>();
            List<IntegralOrigin> integrations = integralService.getIntegralListByUserId(userId);
            for (IntegralOrigin integration : integrations) {
                IntegrationOriginVo vo = new IntegrationOriginVo();
                vo.setOrigin(getIntegrationOrigin(integration.getType())).setCreateTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(integration.getCreateTime()))).setIntegration("+" + integration.getIntegral());
                integrationOrigin.add(vo);
            }
            List<IntegralExchangeRecord> exchanges = integralService.getIntegralExchangeListByUserId(userId);
            for (IntegralExchangeRecord exchange : exchanges) {
                IntegrationOriginVo vo = new IntegrationOriginVo();
                Book book = bookService.getBookById(exchange.getBookId());
                vo.setOrigin(getIntegrationOrigin(exchange.getType()) + "《" + book.getName() + "》").setCreateTime(LocalDateTimeUtil.formatLocalDateTime(LocalDateTimeUtil.parseLocalDateTime(exchange.getCreateTime()))).setIntegration("-" + exchange.getIntegral());
                integrationOrigin.add(vo);
            }
            result.setIntegrationOrigin(integrationOrigin);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
    }

    private String getIntegrationOrigin(Integer type) {
        IntegrationTypeStatus[] types = IntegrationTypeStatus.values();
        for (IntegrationTypeStatus status : types) {
            if (status.getIndex().equals(type))
                return status.getMsg();
        }
        return StringUtils.EMPTY;
    }
}
