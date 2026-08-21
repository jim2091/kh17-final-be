package com.kh.finalprj.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfiguration {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(info())
				.externalDocs(externalDoc());
	}
	
	private Info info() {
		return new Info()
				.title("final project 그룹웨어 REST API")
				.description("KH정보교육원 final 프로젝트 그룹웨어")
				.version("v1.0.0")
				.contact(
					new Contact()
						.name("팀장 이정빈")
						.email("wjdqls2091@gmail.com")
						.url("")
				)
				.license(
					new License()
						.name("MIT License")
						.url("https://opensource.org/license/mit")
				);
	}
	
	private ExternalDocumentation externalDoc() {
		return new ExternalDocumentation()
					.description("깃허브 저장소")
					.url("https://github/jim2091");
	}
	
}
