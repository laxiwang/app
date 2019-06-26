package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.Advertisement;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.AdvertisementService;
import com.jhyx.halfroom.vo.app.AdvertisementVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v4/api/advertisement")
@RequiredArgsConstructor
@Slf4j
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<AdvertisementVo> advertisementImgUrl() {
        try {
            AdvertisementVo result = null;
            Advertisement advertisement = advertisementService.getAdvertisement();
            if (advertisement != null) {
                result = new AdvertisementVo();
                result.setImgUrl(advertisement.getImgUrl()).setRedirectUrl(advertisement.getRedirectUrl());
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }
}
