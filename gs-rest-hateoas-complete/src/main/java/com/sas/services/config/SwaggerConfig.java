package com.sas.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sas.services.controller.ConfigurationController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration file.
 * @author sugawd
 *
 */

@EnableSwagger2
@ComponentScan(basePackageClasses=ConfigurationController.class)
@Configuration
public class SwaggerConfig {
	
	private static final String SWAGGER_API_VERSION = "1.0";
	private static final String LICENSE_ = "No License";
	private static final String TITLE_ = "REST API Documentation";
	private static final String DESCRIPTION = "REST API Documentation";
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(TITLE_)
				.description(DESCRIPTION)
				.license(LICENSE_)
				.version(SWAGGER_API_VERSION)
				.build();
	}
	
	@Bean
	public Docket configurationsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.pathMapping("/")
				.select()
				.paths(PathSelectors.regex("/configurations.*"))
				.build();
				
	}
}
