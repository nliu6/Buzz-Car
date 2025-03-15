package gatech.cs.buzzcar.config;

import gatech.cs.buzzcar.common.interceptor.AuthTokenInterceptor;
import gatech.cs.buzzcar.common.interceptor.PermissionInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthTokenInterceptor authTokenInterceptor;

    @Resource
    private PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> requestHolderList = Arrays.asList("/login", "/public/**");
        registry.addInterceptor(authTokenInterceptor).addPathPatterns("/**").excludePathPatterns(requestHolderList);
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**").excludePathPatterns(requestHolderList);
    }
}