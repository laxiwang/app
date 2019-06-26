package com.jhyx.halfroom.activity.controller;

import com.jhyx.halfroom.bean.*;
import com.jhyx.halfroom.commons.JSONUtil;
import com.jhyx.halfroom.commons.MD5Util;
import com.jhyx.halfroom.constant.*;
import com.jhyx.halfroom.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v4/activity")
@AllArgsConstructor
@Slf4j
public class ActivityController {
    private ActivityExercisesService activityExercisesService;
    private ActivityUserAnswerService activityUserAnswerService;
    private ManualService manualService;
    private UserManualService userManualService;
    private CommonService commonService;
    private UserTeamService userTeamService;
    private UserService userService;
    private ActivityUserPrizeService userPrizeService;
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/user/browse/page", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Boolean> statisticsParticipationUserCount(@RequestParam String pageName,
                                                            @RequestParam String browseWay,
                                                            HttpServletRequest request) {
        try {
            //封装用户最新的浏览页面
            Map<String, String> page = new HashMap<>();
            page.put(pageName, browseWay);
            //用户所有的浏览页面
            List<Map> userBrowsePages;
            Long userId = commonService.getUserIdNeedLogin(request);
            //查询得到结果
            String browsePageList = redisTemplate.opsForValue().get(RedisKeyPrefix.ACTIVITY_USER_BROWSE_PAGE + userId);
            if (StringUtils.isNotEmpty(browsePageList)) {
                userBrowsePages = JSONUtil.parseList(browsePageList, Map.class);
            } else {
                userBrowsePages = new ArrayList<>();
            }
            //添加最新的页面
            userBrowsePages.add(page);
            //写回到redis
            redisTemplate.opsForValue().set(RedisKeyPrefix.ACTIVITY_USER_BROWSE_PAGE + userId, JSONUtil.toJSONString(userBrowsePages), 90, TimeUnit.DAYS);
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PostMapping(value = "/receive/gift", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> receiveGift(@RequestParam String userName,
                                      @RequestParam String userAddress,
                                      @RequestParam String userPhone,
                                      HttpServletRequest request) {
        try {
            boolean flag = false;
            Long userId = commonService.getUserIdNeedLogin(request);
            UserTeam userTeam = userTeamService.getUserTeamByUserId(userId);
            if (userTeam == null) {
                return new Result<>(ResultCode.USER_NOT_QUALIFICATION.getIndex(), ResultCode.USER_NOT_QUALIFICATION.getMsg(), null);
            }
            List<UserTeam> list = userTeamService.getUserTeamByTeamId(userTeam.getTeamId());
            if (list != null && list.size() > 3) {
                Set<Long> ids = list.stream().map(UserTeam::getUserId).collect(Collectors.toSet());
                Collection<ActivityUserAnswer> userAnswerList = activityUserAnswerService.getActivityUserAnswerByUserIds(ids);
                long count = userAnswerList.stream().filter(au -> au.getRecordHigh() == 10).count();
                if (userTeam.getUserType().equals(UserTypeTeamStatus.TEAM_MASTER.getIndex())) {
                    if (count > 1) {
                        flag = true;
                    }
                } else {
                    if (count > 1) {
                        ActivityUserAnswer activityUserAnswer = userAnswerList.stream().filter(au -> au.getUserId().equals(userId) && au.getRecordHigh() == 10).findFirst().orElseGet(ActivityUserAnswer::new);
                        if (activityUserAnswer.getUserId() != null) {
                            flag = true;
                        }
                    }
                }
            }
            if (!flag) {
                return new Result<>(ResultCode.USER_NOT_QUALIFICATION.getIndex(), ResultCode.USER_NOT_QUALIFICATION.getMsg(), null);
            }
            ActivityUserPrize userPrize = userPrizeService.getUserPrizeByUserId(userId);
            if (userPrize == null) {
                userPrize = new ActivityUserPrize();
                userPrize.setUserId(userId).setUserName(userName).setUserAddress(userAddress).setUserPhone(userPhone);
                userPrizeService.saveUserPrize(userPrize);
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), null);
            } else {
                return new Result<>(ResultCode.USER_HAS_RECEIVE_GIFT.getIndex(), ResultCode.USER_HAS_RECEIVE_GIFT.getMsg(), null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/share/answer/image", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> shareImage(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setBgImageName("http://static.half-room.com/a182598bd48e2614.png");
            shareInfo.setRedirectUrl("http://activity.half-room.com/active/#/answerActive?introducerId=" + userId);
            shareInfo.setQrW(160).setQrH(160).setQrX(766).setQrY(1308).setFontX(135).setFontY(1379).setFontColorR(255).setFontColorG(255).setFontColorB(255).setFontSize(42).setFontStyle("微软雅黑");
            String key = MD5Util.getMD5Code(shareInfo.getRedirectUrl() + shareInfo.getBgImageName() + user.getName()) + ".jpg";
            String imageUrl = commonService.getShareImageUrl(shareInfo, key, shareInfo.getRedirectUrl(), "我是 " + user.getName());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), imageUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/team/invite/image", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> teamInviteImage(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            UserTeam userTeam = userTeamService.getUserTeamByUserId(userId);
            if (user == null || userTeam == null) {
                return new Result<>(ResultCode.ARGS_ERROR.getIndex(), ResultCode.ARGS_ERROR.getMsg(), null);
            }
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setBgImageName("http://static.half-room.com/200e115bbfba5467ww.png");
            shareInfo.setRedirectUrl("http://activity.half-room.com/active/#/joinTeam?introducerId=" + userId + "&teamId=" + userTeam.getTeamId() + "&introducerName=" + user.getName());
            shareInfo.setQrW(160).setQrH(160).setQrX(766).setQrY(1308)
                    .setFontSize(56).setFontColorR(118).setFontColorG(13).setFontColorB(255).setFontStyle("微软雅黑").setFontX(271).setFontY(833)
                    .setFontSizeNext(42).setFontColorRNext(255).setFontColorGNext(255).setFontColorBNext(255).setFontStyleNext("微软雅黑").setFontXNext(134).setFontYNext(1379);
            String key = MD5Util.getMD5Code(shareInfo.getRedirectUrl() + shareInfo.getBgImageName() + user.getName() + new Date()) + ".jpg";
            String imageUrl = commonService.getShareImageUrl(shareInfo, key, shareInfo.getRedirectUrl(), user.getName(), "我是 " + user.getName());
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), imageUrl);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/team/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, Object>> teamDetail(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            UserTeam userTeam = userTeamService.getUserTeamByUserId(userId);
            if (userTeam == null) {
                return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), null);
            }
            List<UserTeam> list = userTeamService.getUserTeamByTeamId(userTeam.getTeamId());
            List<TeamAnswerInfo> teamAnswerList = getTeamAnswerInfo(list, userId);
            Map<String, Object> result = new HashMap<>();
            result.put("userType", userTeam.getUserType());
            result.put("teamId", userTeam.getTeamId());
            result.put("teamAnswer", teamAnswerList);
            if (userTeam.getUserType().equals(UserTypeTeamStatus.TEAM_MASTER.getIndex())) {
                if (list.size() < 4) {
                    result.put("teamStatus", 0);
                    result.put("inviteCount", list.size() - 1);
                } else {
                    result.put("teamStatus", 1);
                    int count = 0;
                    for (TeamAnswerInfo teamAnswerInfo : teamAnswerList) {
                        if (teamAnswerInfo.getCount() == 10) {
                            count++;
                        }
                    }
                    if (count >= 2) {
                        result.put("teamStatus", 2);
                    }
                }
            } else {
                result.put("flag", judgeUserHasPermission(userId, userTeam.getTeamId()));
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PutMapping(value = "/modify/user/team", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Map<String, Object>> saveUserTeam(@RequestParam(required = false) Long teamId,
                                                    HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            UserTeam userTeam = userTeamService.getUserTeamByUserId(userId);
            if (userTeam != null && !userTeam.getTeamId().equals(teamId)) {
                return new Result<>(ResultCode.USER_HAD_TEAM.getIndex(), ResultCode.USER_HAD_TEAM.getMsg(), null);
            }
            if (userTeam == null) {
                if (teamId == null) {
                    userTeam = new UserTeam();
                    userTeam.setTeamId(userId).setUserId(userId).setUserType(UserTypeTeamStatus.TEAM_MASTER.getIndex());
                    teamId = userId;
                } else {
                    userTeam = new UserTeam();
                    userTeam.setTeamId(teamId).setUserId(userId).setUserType(UserTypeTeamStatus.TEAM_GENERAL.getIndex());
                }
                userTeamService.saveUserTeam(userTeam);
                //如果加入战队的人数达到4人及以上，赠送老师素材
                List<UserTeam> userTeamList = userTeamService.getUserTeamByTeamId(teamId);
                if (userTeamList != null && userTeamList.size() == 4) {
                    UserTeam team = userTeamList.stream().filter(ut -> ut.getTeamId().equals(ut.getUserId())).findFirst().orElseGet(UserTeam::new);
                    if (team.getUserId() != null) {
                        List<Manual> list = manualService.getManualByType(ManualTypeStatus.TEACHER_MANUAL.getIndex());
                        UserManual userManual = new UserManual();
                        userManual.setUserId(team.getUserId()).setManualId(list.get(0).getId());
                        userManualService.batchSaveUserManualList(List.of(userManual));
                    }
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), null);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/topic", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Result<List<Map<String, Object>>> topic(@RequestParam(required = false) Integer userType,
                                                   HttpServletRequest request) {
        try {
            List<Map<String, Object>> result = null;
            Long userId = commonService.getUserIdNoNeedLogin(request);
            if (userId != null) {
                ActivityUserAnswer activityUserAnswer = activityUserAnswerService.getActivityUserAnswerByUserId(userId);
                if (activityUserAnswer != null) {
                    userType = activityUserAnswer.getExercisesType();
                }
            }
            if (userType == null) {
                return new Result<>(ResultCode.ARGS_ERROR.getIndex(), ResultCode.ARGS_ERROR.getMsg(), null);
            }
            List<ActivityExercises> list = activityExercisesService.getActivityExercisesListByUserType(userType);
            if (list != null && list.size() > 0) {
                result = new ArrayList<>();
                if (userType.equals(ActivityExercisesUserTypeStatus.JUNIOR_SCHOOL_STUDENT.getIndex())) {
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.SIMPLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.MIDDLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.DIFFICULTY.getIndex(), 1);
                    packageFields(result, list, ActivityExercisesTypeStatus.HISTORY.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.SIMPLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.HISTORY.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.MIDDLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.HISTORY.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.DIFFICULTY.getIndex(), 1);
                } else {
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.SIMPLE.getIndex(), 4);
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.MIDDLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.CHINESE.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.DIFFICULTY.getIndex(), 1);
                    packageFields(result, list, ActivityExercisesTypeStatus.HISTORY.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.SIMPLE.getIndex(), 2);
                    packageFields(result, list, ActivityExercisesTypeStatus.HISTORY.getIndex(), ActivityExercisesDifficultyLevelTypeStatus.MIDDLE.getIndex(), 1);
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @PutMapping(value = "/user/answer", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Result<Boolean> userAnswer(@RequestParam Integer historyCount,
                                      @RequestParam Integer chineseCount,
                                      @RequestParam Integer exercisesType,
                                      HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            ActivityUserAnswer activityUserAnswer = activityUserAnswerService.getActivityUserAnswerByUserId(userId);
            if (activityUserAnswer == null) {
                activityUserAnswer = new ActivityUserAnswer();
                activityUserAnswer.setChineseCount(chineseCount).setHistoryCount(historyCount).setUserId(userId).setExercisesType(exercisesType).setRecordHigh(historyCount + chineseCount);
                activityUserAnswerService.saveActivityUserAnswer(activityUserAnswer);
            } else {
                activityUserAnswer.setChineseCount(chineseCount).setHistoryCount(historyCount);
                if (chineseCount + historyCount > activityUserAnswer.getRecordHigh()) {
                    activityUserAnswer.setRecordHigh(chineseCount + historyCount);
                }
                activityUserAnswerService.updateActivityUserAnswer(activityUserAnswer);
            }
            //判断是否需要赠送知识手册
            if (activityUserAnswer.getChineseCount() + activityUserAnswer.getHistoryCount() > 3) {
                List<Manual> manualList = manualService.getManualByType(ManualTypeStatus.GENERAL_MANUAL.getIndex());
                List<UserManual> userManualList = userManualService.getUserManualByUserId(userId);
                if (userManualList == null || userManualList.size() == 0) {
                    userManualList = packageUserManualList(userId, manualList);
                    userManualService.batchSaveUserManualList(userManualList);
                } else {
                    boolean flag = false;
                    for (Manual manual : manualList) {
                        for (UserManual userManual : userManualList) {
                            if (userManual.getManualId().equals(manual.getId())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }
                    if (!flag) {
                        userManualList = packageUserManualList(userId, manualList);
                        userManualService.batchSaveUserManualList(userManualList);
                    }
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    @GetMapping(value = "/answer/detail", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Result<Map<String, Object>> answerDetail(HttpServletRequest request) {
        try {
            Long userId = commonService.getUserIdNeedLogin(request);
            User user = userService.getUserById(userId);
            ActivityUserAnswer userAnswer = activityUserAnswerService.getActivityUserAnswerByUserId(userId);
            if (userAnswer == null) {
                return new Result<>(ResultCode.ARGS_ERROR.getIndex(), ResultCode.ARGS_ERROR.getMsg(), null);
            }
            Map<String, Object> result = new HashMap<>();
            TitleTypeStatus titleType = null;
            int history = userAnswer.getHistoryCount();
            int chinese = userAnswer.getChineseCount();
            int count = history + chinese;
            switch (count) {
                case 4:
                    if (history == 0 || history == 1) {
                        titleType = TitleTypeStatus.POETRY_MEMORY_MASTER;
                    } else if (history == 2) {
                        titleType = TitleTypeStatus.POETRY_HISTORY_NEW_STAR;
                    } else {
                        titleType = TitleTypeStatus.HISTORY_MEMORY_MASTER;
                    }
                    break;
                case 5:
                    if (history == 0 || history == 1) {
                        titleType = TitleTypeStatus.POETRY_AMATEUR;
                    } else if (history == 2 || history == 3) {
                        titleType = TitleTypeStatus.POETRY_HISTORY_ALMIGHTY;
                    } else {
                        titleType = TitleTypeStatus.HISTORY_AMATEUR;
                    }
                    break;
                case 6:
                    if (history == 0 || history == 1) {
                        titleType = TitleTypeStatus.POETRY_AMATEUR;
                    } else if (history == 2 || history == 3 || history == 4) {
                        titleType = TitleTypeStatus.POETRY_HISTORY_ALMIGHTY;
                    } else {
                        titleType = TitleTypeStatus.HISTORY_AMATEUR;
                    }
                    break;
                case 7:
                    if (history == 0 || history == 1 || history == 2) {
                        titleType = TitleTypeStatus.POETRY_MASTER;
                    } else if (history == 3 || history == 4) {
                        titleType = TitleTypeStatus.POETRY_HISTORY_MASTER;
                    } else {
                        titleType = TitleTypeStatus.HISTORY_MASTER;
                    }
                    break;
                case 8:
                    if (history == 1 || history == 2) {
                        titleType = TitleTypeStatus.POETRY_MASTER;
                    } else if (history == 3 || history == 4) {
                        titleType = TitleTypeStatus.POETRY_HISTORY_MASTER;
                    } else {
                        titleType = TitleTypeStatus.HISTORY_MASTER;
                    }
                    break;
                case 9:
                    if (history == 2) {
                        titleType = TitleTypeStatus.POETRY_KING;
                    } else {
                        titleType = TitleTypeStatus.POETRY_HISTORY_KING;
                    }
                    break;
                case 10:
                    titleType = TitleTypeStatus.POETRY_HISTORY_KING;
                    break;
            }
            result.put("chineseCount", chinese);
            result.put("historyCount", history);
            result.put("count", count);
            result.put("hasTeam", false);
            result.put("flag", false);
            UserTeam userTeam = userTeamService.getUserTeamByUserId(userId);
            if (userTeam != null) {
                result.put("hasTeam", true);
                result.put("flag", judgeUserHasPermission(userId, userTeam.getTeamId()));
            }
            if (titleType != null) {
                ShareInfo shareInfo = new ShareInfo();
                shareInfo.setBgImageName(titleType.getBigUrl()).setQrH(160).setQrW(160).setQrX(766).setQrY(1308).setFontX(135).setFontY(1379).setFontColorR(118).setFontColorG(13).setFontColorB(255).setFontSize(42).setFontStyle("微软雅黑");
                String url = "http://activity.half-room.com/active/#/answerActive?introducerId=" + userId;
                String key = MD5Util.getMD5Code(url + shareInfo.getBgImageName() + user.getName()) + ".jpg";
                result.put("smallImageUrl", titleType.getSmallUrl());
                result.put("bigImageUrl", commonService.getShareImageUrl(shareInfo, key, url, "我是 " + user.getName()));
                List<ActivityUserAnswer> list = activityUserAnswerService.getAllActivityUserAnswer();
                if (list != null && list.size() > 0) {
                    List<UserAnswerInfo> li = new ArrayList<>();
                    for (ActivityUserAnswer au : list) {
                        if (au != null && au.getUserId() != null && au.getHistoryCount() != null && au.getChineseCount() != null) {
                            UserAnswerInfo userAnswerInfo = new UserAnswerInfo();
                            userAnswerInfo.setUserId(au.getUserId()).setCount(au.getChineseCount() + au.getHistoryCount());
                            li.add(userAnswerInfo);
                        }
                    }
                    if (li.size() > 0) {
                        li.sort(Comparator.comparingInt(UserAnswerInfo::getCount));
                        Collections.reverse(li);
                        li.removeIf(ui -> (ui.getCount() == userAnswer.getChineseCount() + userAnswer.getHistoryCount()) && !(ui.getUserId().equals(userAnswer.getUserId())));
                        String percentage = StringUtils.EMPTY;
                        if (li.size() == 1) {
                            percentage = "100%";
                        } else {
                            for (int i = 0; i < li.size(); i++) {
                                if (li.get(i).getUserId().equals(userAnswer.getUserId())) {
                                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                                    decimalFormat.setRoundingMode(RoundingMode.FLOOR);
                                    double value = (double) (li.size() - 1 - i) / (double) (li.size() - 1);
                                    value = Double.parseDouble(decimalFormat.format(value)) * 100;
                                    percentage = (int) value + "%";
                                    break;
                                }
                            }
                        }
                        result.put("percentage", percentage);
                    }
                }
            }
            return new Result<>(ResultCode.SUCCESS.getIndex(), ResultCode.SUCCESS.getMsg(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new Result<>(ResultCode.ERROR.getIndex(), ResultCode.ERROR.getMsg(), null);
        }
    }

    private List<UserManual> packageUserManualList(Long userId, List<Manual> manualList) {
        List<UserManual> result = null;
        if (userId != null && manualList != null && manualList.size() > 0) {
            result = new ArrayList<>();
            for (Manual manual : manualList) {
                UserManual userManual = new UserManual();
                userManual.setUserId(userId).setManualId(manual.getId());
                result.add(userManual);
            }
        }
        return result;
    }

    private void packageFields(List<Map<String, Object>> result, List<ActivityExercises> list, Integer exercisesType, Integer difficultyLevelType, Integer count) {
        if (result == null || list == null || list.size() == 0 || exercisesType == null || difficultyLevelType == null || count == null) {
            return;
        }
        list = list.stream().filter(ac -> ac.getExercisesType().equals(exercisesType)).filter(ac -> ac.getExercisesDifficultyLevel().equals(difficultyLevelType)).collect(Collectors.toList());
        for (int i = 0; i < count; i++) {
            Map<String, Object> map = new HashMap<>();
            Random random = new Random();
            int index = random.nextInt(list.size());
            ActivityExercises activityExercises = list.get(index);
            list.remove(activityExercises);
            map.put("title", activityExercises.getTitle());
            map.put("answer", activityExercises.getAnswer());
            map.put("tips", activityExercises.getTips());
            map.put("type", activityExercises.getExercisesType());
            String[] option = activityExercises.getOptions().split(",");
            Map<Character, String> options = new HashMap<>();
            for (int j = 0; j < option.length; j++) {
                options.put((char) ('A' + j), option[j]);
            }
            map.put("options", options);
            result.add(map);
        }
    }

    private List<TeamAnswerInfo> getTeamAnswerInfo(List<UserTeam> list, Long userId) {
        List<TeamAnswerInfo> args = new ArrayList<>();
        if (list != null && list.size() > 0) {
            Long masterId = list.get(0).getTeamId();
            Set<Long> userIds = list.stream().map(UserTeam::getUserId).collect(Collectors.toSet());
            List<User> userList = userService.getUserListByIds(userIds);
            Collection<ActivityUserAnswer> userAnswerCollection = activityUserAnswerService.getActivityUserAnswerByUserIds(userIds);
            for (User user : userList) {
                TeamAnswerInfo teamAnswerInfo = new TeamAnswerInfo();
                teamAnswerInfo.setName(user.getName()).setCount(0).setUserId(user.getId());
                for (ActivityUserAnswer activityUserAnswer : userAnswerCollection) {
                    if (activityUserAnswer.getUserId().equals(user.getId())) {
                        teamAnswerInfo.setCount(activityUserAnswer.getRecordHigh());
                        break;
                    }
                }
                args.add(teamAnswerInfo);
            }
            int masterIndex = -1;
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).getUserId().equals(masterId)) {
                    masterIndex = i;
                    break;
                }
            }
            if (masterIndex > 0) {
                swap(args, 0, masterIndex);
            }
            masterIndex = -1;
            if (!userId.equals(masterId)) {
                for (int i = 0; i < args.size(); i++) {
                    if (args.get(i).getUserId().equals(userId)) {
                        masterIndex = i;
                        break;
                    }
                }
                if (masterId > 1) {
                    swap(args, 1, masterIndex);
                }
            }
        }
        return args;
    }

    private void swap(List<TeamAnswerInfo> list, int index1, int index2) {
        TeamAnswerInfo temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    private boolean judgeUserHasPermission(Long userId, Long teamId) {
        boolean result = false;
        List<UserTeam> userTeamList = userTeamService.getUserTeamByTeamId(teamId);
        if (userTeamList != null && userTeamList.size() > 3) {
            Set<Long> ids = userTeamList.stream().map(UserTeam::getUserId).collect(Collectors.toSet());
            Collection<ActivityUserAnswer> userAnswerList = activityUserAnswerService.getActivityUserAnswerByUserIds(ids);
            int count = 0;
            for (ActivityUserAnswer userAnswer : userAnswerList) {
                if (userAnswer.getRecordHigh() == 10) {
                    count++;
                }
            }
            if (count > 1) {
                for (ActivityUserAnswer userAnswer : userAnswerList) {
                    if (userAnswer.getUserId().equals(userId) && userAnswer.getRecordHigh() == 10) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    @Data
    @Accessors(chain = true)
    private class UserAnswerInfo {
        private Long userId;
        private int count;
    }

    @Data
    @Accessors(chain = true)
    private class TeamAnswerInfo {
        private Long userId;
        private String name;
        private int count;
    }
}