package com.jhyx.halfroom.api.controller;

import com.jhyx.halfroom.bean.AppVersion;
import com.jhyx.halfroom.constant.AppVersionStatus;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.AppVersionService;
import com.jhyx.halfroom.vo.app.AppVersionVo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v4/api/system")
@AllArgsConstructor
public class SystemController {
    private AppVersionService appVersionService;

    @GetMapping(value = "/version/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<AppVersionVo> checkVersion(@RequestParam(defaultValue = "android", required = false) String os,
                                             @RequestParam String version) {
        List<AppVersion> list = appVersionService.getAppVersionByOS(os);
        if (list == null || list.size() == 0)
            return new Result<>(ResultCode.INTERFACE_NOT_OPEN.getIndex(), ResultCode.INTERFACE_NOT_OPEN.getMsg(), null);
        AppVersion lastVersion = null;
        String lastVersionCode = version;
        for (AppVersion av : list) {
            if (av.getVersion().compareTo(lastVersionCode) > 0) {
                lastVersion = av;
                lastVersionCode = av.getVersion();
            }
        }
        AppVersionVo vo = new AppVersionVo();
        if (lastVersion != null)
            vo.setContent(lastVersion.getContent()).setUpGrade(lastVersion.getUpGrade()).setMd5(lastVersion.getMd5()).setPackUrl(lastVersion.getPackUrl()).setVersion(lastVersion.getVersion());
        else
            vo.setUpGrade(AppVersionStatus.NO_UPGRADE.getIndex());
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), vo);
    }
}
