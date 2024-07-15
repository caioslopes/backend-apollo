package br.com.apollomusic.app.infra.config;

import br.com.apollomusic.app.infra.ApiTokenInterceptor;
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
        registry.addInterceptor(apiTokenInterceptor).excludePathPatterns("/auth/owner/**", "/auth/user/**", "/user/**");
    }
}
