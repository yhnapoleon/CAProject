//By Xu Wenzhe and Zhao Jiayi

package sg.nusiss.t6.caproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ImageConfig implements WebMvcConfigurer {

    @Value("${app.image-path}")
    private String imagePath;

    @Value("${app.avatar-path}")
    private String avatarPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imagePath);

        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + avatarPath);
    }
}
