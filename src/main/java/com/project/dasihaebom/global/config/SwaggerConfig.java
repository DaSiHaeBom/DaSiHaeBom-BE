package com.project.dasihaebom.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI DaSiHaeBomAPI() {
        Info info = new Info()
                .title("DaSiHaeBomAPI") // API 제목
                .description("LikeLion13th 대 상 명 API 명세서") // 설명
                .version("1.0.0"); //버전

        String csrfSchemeName = "X-XSRF-TOKEN";
        String csrfCookieName = "XSRF-TOKEN";
        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(csrfSchemeName).addList(csrfCookieName);

        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(csrfSchemeName, new SecurityScheme()
                        .name(csrfSchemeName)
                        .type(SecurityScheme.Type.APIKEY) // csrf token은 api key 방식
                        .in(SecurityScheme.In.HEADER) // header에 위치
                        .description("csrf token을 헤더에 입력하세요"));

        components.addSecuritySchemes(csrfCookieName,
                new SecurityScheme()
                        .name(csrfCookieName)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE)
                        .description("csrf token 쿠키 입력"));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
