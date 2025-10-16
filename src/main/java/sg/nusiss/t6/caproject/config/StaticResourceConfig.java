package sg.nusiss.t6.caproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.image-path:E:/images/}")
    private String uploadDir;

    @Value("${app.upload.public-prefix:/images}")
    private String publicPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = uploadPath.toUri().toString();
        String pattern = (publicPrefix.endsWith("/") ? publicPrefix : publicPrefix + "/") + "**";
        registry.addResourceHandler(pattern)
                .addResourceLocations(location);
    }
}
