//By Ying Hao

package sg.nusiss.t6.caproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(name = "bearer-jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(security = { @SecurityRequirement(name = "bearer-jwt") })
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CAProject API")
                        .version("v1"))
                .components(new Components().addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                .name(HttpHeaders.AUTHORIZATION)))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
