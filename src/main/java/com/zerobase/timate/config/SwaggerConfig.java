package com.zerobase.timate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList("Authorization"))
			.components(new Components()
				.addSecuritySchemes("Authorization",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				)
			);
	}


	private Info apiInfo() {
		return new Info()
			.title("Timate")
			.description("Timate API Documentation")
			.version("v1");
	}

}
