package com.jhyx.halfroom.common.controller;

import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.PhoneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v4/common/send/phone")
@AllArgsConstructor
@Slf4j
public class CommonPhoneController {
    private PhoneService phoneService;

    @GetMapping(value = "/code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> sendPhoneCode(@RequestParam String phone) {
        Boolean flag = phoneService.sendLoginCode(phone);
        if(flag != null && flag)
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        else
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), Boolean.FALSE);
    }
}
