package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.Banner;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.BannerService;
import com.jhyx.halfroom.vo.app.BannerVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v4/api/banner")
@Slf4j
public class BannerController {
    private final BannerService bannerService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<BannerVo>> bannerList() {
        List<BannerVo> results = new ArrayList<>();
        try {
            List<Banner> list = bannerService.bannerList();
            if (list != null && list.size() > 0) {
                list.forEach(banner -> results.add(new BannerVo().setImgUrl(banner.getImgUrl()).setRedirectUrl(banner.getRedirectUrl()).setType(banner.getType())));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), results);
    }
}
