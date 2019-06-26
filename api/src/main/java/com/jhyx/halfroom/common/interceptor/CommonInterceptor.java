package com.jhyx.halfroom.common.interceptor;

import com.jhyx.halfroom.commons.JSONUtil;
import com.jhyx.halfroom.constant.Result;
import com.jhyx.halfroom.constant.ResultCode;
import com.jhyx.halfroom.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class CommonInterceptor extends HandlerInterceptorAdapter {
    private UserLoginService userLoginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            response.getWriter().println(JSONUtil.toJSONString(new Result<String>(ResultCode.USER_NOT_LOGIN.getIndex(), ResultCode.USER_NOT_LOGIN.getMsg(), null)));
            return Boolean.FALSE;
        }
        Long userId = userLoginService.getUserLoginByToken(token);
        if (userId == null) {
            response.getWriter().println(JSONUtil.toJSONString(new Result<String>(ResultCode.USER_NOT_LOGIN.getIndex(), ResultCode.USER_NOT_LOGIN.getMsg(), null)));
            return Boolean.FALSE;
        }
        request.setAttribute("userId", userId);
        return Boolean.TRUE;
    }
}
