package com.project.dasihaebom.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {

    // 외부 api에 요청을 보내고 응답을 받는 역할을 하는 도구라고 합니다요
    @Bean(name = "corpNumberWebClient")
    public WebClient corpNumberWebClient(
            @Value("${spring.corp.base-url}") String baseUrl
    ) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        // 더 이상 uri를 인코딩 하지 않음
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
        // NONE을 사용하지 않는 경우 uri 인코딩 하는 케이스
//        return WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }

    @Bean(name = "phoneNumberWebClient")
    // 핸드폰 번호 인증 웹 클라이언트
    public WebClient phoneNumberWebClient(
            @Value("${spring.message.base-url}") String baseUrl
    ) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        // 더 이상 uri를 인코딩 하지 않음
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=UTF-8")
                .build();
    }

    @Bean(name = "coordinateWebClient")
    // 핸드폰 번호 인증 웹 클라이언트
    public WebClient coordinateWebClient(
            @Value("${spring.kakao.local-api.base-url}") String baseUrl
    ) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        // 더 이상 uri를 인코딩 하지 않음
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=UTF-8")
                .build();
    }
}
