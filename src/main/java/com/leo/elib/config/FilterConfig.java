package com.leo.elib.config;

import com.leo.elib.filter.ATFilter;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Resource
    private ATFilter atFilter;
    @Bean
    public FilterRegistrationBean<ATFilter> jwtFilter() {
        FilterRegistrationBean<ATFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(atFilter);
        registrationBean.addUrlPatterns("/*"); // 没办法设置不拦截的路径，所以只能在过滤器中设置，这里设置为全部拦截
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
