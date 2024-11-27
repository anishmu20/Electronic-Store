package pros.ElectronicStore.projectConfig;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electronic Store API")
                        .version("1.0.0")
                        .contact(new Contact().name("Anish Mu").email("anishmu302@gmail.com").url("https://github.com/anishmu20"))
                        .description("API documentation for managing e-store products online")
                        ).servers(List.of(
                        new Server().url("http://localhost:9090").description("Local environment"),
                        new Server().url("https://api.electronicstore.com").description("Production environment")
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
