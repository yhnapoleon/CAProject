package sg.nusiss.t6.caproject.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("CA Project API")
            .description("Shopping Cart 后端接口文档")
            .version("v1")
            .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
            .contact(new Contact().name("Team T6").email("team-t6@example.com")))
        .externalDocs(new ExternalDocumentation()
            .description("Project README")
            .url("https://example.com"));
  }
}
