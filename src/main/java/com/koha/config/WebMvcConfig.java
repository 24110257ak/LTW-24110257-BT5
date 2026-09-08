package com.koha.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.koha.filter.MySiteMeshFilter;
import com.koha.util.Constant;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<MySiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<MySiteMeshFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new MySiteMeshFilter());
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + Constant.DIR + "/");
        registry.addResourceHandler("/static/**", "/assets/**", "/resources/**")
                .addResourceLocations("classpath:/static/", "classpath:/assets/", "/");
    }
}
