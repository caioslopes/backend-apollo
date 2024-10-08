package br.com.apollomusic.app.infra.config;

import br.com.apollomusic.app.infra.interceptor.ApiTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiTokenInterceptor apiTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiTokenInterceptor).excludePathPatterns("/auth/owner/**", "/establishment/user/**", "/establishment/{id}", "/establishment/playlist/genres/{establishmentId}", "/establishment/new", "/owner/new", "/ping");
    }
}
