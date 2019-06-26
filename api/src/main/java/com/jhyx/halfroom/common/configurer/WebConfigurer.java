package com.jhyx.halfroom.common.configurer;

import com.jhyx.halfroom.common.filter.CorsFilter;
import com.jhyx.halfroom.common.interceptor.CommonInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class WebConfigurer implements WebMvcConfigurer {
    private final CommonInterceptor commonInterceptor;
    private List<String> patterns = new ArrayList<>();

    {
        patterns.add("/v4/activity/user/browse/page");
        patterns.add("/v4/activity/receive/gift");
        patterns.add("/v4/activity/share/answer/image");
        patterns.add("/v4/activity/team/invite/image");
        patterns.add("/v4/activity/team/detail");
        patterns.add("/v4/activity/modify/user/team");
        patterns.add("/v4/activity/answer/detail");
        patterns.add("/v4/activity/user/answer");
        patterns.add("/v4/api/user/info");
        patterns.add("/v4/api/user/register/tips");
        patterns.add("/v4/api/user/invite/message");
        patterns.add("/v4/api/user/invite/message/user/read/status");
        patterns.add("/v4/api/user/share/image");
        patterns.add("/v4/api/user/study/punch/time");
        patterns.add("/v4/api/user/update/info");
        patterns.add("/v4/api/user/feed/back");
        patterns.add("/v4/api/user/recharge/record");
        patterns.add("/v4/api/user/play/record/history");
        patterns.add("/v4/api/user/purchase/book");
        patterns.add("/v4/api/wx/pay/order");
        patterns.add("/v4/api/apple/pay/order");
        patterns.add("/v4/api/book/share/info");
        patterns.add("/v4/api/book/presentation/info");
        patterns.add("/v4/common/book/upload/play/end");
        patterns.add("/v4/common/upload/answer/info");
        patterns.add("/v4/common/integration/detail");
        patterns.add("/v4/common/integration/exchange/goods");
        patterns.add("/v4/common/integration/exchange/history");
        patterns.add("/v4/common/user/integration");
        patterns.add("/v4/common/user/virtual/point");
        patterns.add("/v4/common/user/recharge/list");
        patterns.add("/v4/wap/user/purchased/book");
        patterns.add("/v4/wap/share/home/page/image");
        patterns.add("/v4/wap/wx/pay/order");
        patterns.add("/v4/common/comment/create");
        patterns.add("/v4/common/comment/praise");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns(patterns);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> registration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(corsFilter);
        Set<String> patterns = new HashSet<>();
        patterns.add("/*");
        registration.setUrlPatterns(patterns);
        return registration;
    }
}
