package sg.nusiss.t6.caproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有接口路径
                .allowedOriginPatterns("*") // 允许所有来源（使用allowedOriginPatterns而不是allowedOrigins）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许携带cookie
    }
}

