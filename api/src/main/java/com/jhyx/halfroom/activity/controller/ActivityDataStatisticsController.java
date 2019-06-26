package com.jhyx.halfroom.activity.controller;

import com.jhyx.halfroom.bean.ActivityUserAnswer;
import com.jhyx.halfroom.bean.UserManual;
import com.jhyx.halfroom.bean.UserTeam;
import com.jhyx.halfroom.commons.JSONUtil;
import com.jhyx.halfroom.constant.RedisKeyPrefix;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.ActivityUserAnswerService;
import com.jhyx.halfroom.service.UserManualService;
import com.jhyx.halfroom.service.UserTeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v4/activity")
@AllArgsConstructor
@Slf4j
public class ActivityDataStatisticsController {
    private StringRedisTemplate redisTemplate;
    private ActivityUserAnswerService activityUserAnswerService;
    private UserManualService userManualService;
    private UserTeamService userTeamService;

    @PostMapping(value = "/gift", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> gift() {
        Set<UserManual> result = new HashSet<>();
        List<ActivityUserAnswer> userAnswerList = activityUserAnswerService.getAllActivityUserAnswer();
        List<UserTeam> userTeamList = userTeamService.getUserTeamByTeamId(null);
        List<UserManual> userManualList = userManualService.getUserManualByUserId(null);
        for (ActivityUserAnswer userAnswer : userAnswerList) {
            Long userId = userAnswer.getUserId();
            UserTeam userTeam = userTeamList.stream().filter(ut -> ut.getUserId().equals(userId)).findFirst().orElseGet(UserTeam::new);
            if (userTeam.getTeamId() != null) {
                List<UserTeam> teamGeneralList = userTeamList.stream().filter(ut -> ut.getTeamId().equals(userTeam.getTeamId())).collect(Collectors.toList());
                if (teamGeneralList.size() > 2) {
                    int count = 0;
                    for (UserTeam ut : teamGeneralList) {
                        for (ActivityUserAnswer ua : userAnswerList) {
                            if (ut.getUserId().equals(ua.getUserId()) && ua.getRecordHigh() == 10) {
                                count++;
                            }
                            if (count > 1) {
                                break;
                            }
                        }
                    }
                    if (count > 1) {
                        for (UserTeam ut : teamGeneralList) {
                            boolean flag = false;
                            for (UserManual userManual : userManualList) {
                                if (ut.getUserId().equals(userManual.getUserId()) && userManual.getManualId() == 3) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                result.add(new UserManual().setManualId(3).setUserId(ut.getUserId()));
                            }
                        }
                    }
                }
            }
        }
        userManualService.batchSaveUserManualList(result);
        return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), null);
    }

    @GetMapping(value = "/data/statistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Integer dataStatistics() {
        int count = 0;
        Set<String> keys = redisTemplate.keys(RedisKeyPrefix.ACTIVITY_USER_BROWSE_PAGE + "*");
        //所有人的活动参与流程
        List<String> list = redisTemplate.opsForValue().multiGet(keys);
        if (list != null && list.size() > 0) {
            for (String str : list) {
                //每个人的活动参与流程
                List<Map> maps = JSONUtil.parseList(str, Map.class);
                boolean flag = false;
                for (Map<String, String> map : maps) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        //判断该人是否是从APP端进入
                        if (entry.getKey().equals("/answerActive") && entry.getValue().equals("APP")) {
                            count++;
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            }
        }
        return count;
    }
}
