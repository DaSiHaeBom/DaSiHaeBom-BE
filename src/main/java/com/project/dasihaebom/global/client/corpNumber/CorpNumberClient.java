package com.project.dasihaebom.global.client.corpNumber;

import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.global.client.corpNumber.dto.NtsCorpInfoResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorpNumberClient {

    private final WebClient corpNumberWebClient;

    @Value("${spring.corp.api-key}")
    private String serviceKey;

    public NtsCorpInfoResDto.CorpInfo getCorpInfo(String corpNumber) {
        return corpNumberWebClient.post()
                // EncodingMode.NONE 이므로 인코딩 하지는 않음
                .uri(uriBuilder -> uriBuilder
                        .path("/status")
                        .queryParam("serviceKey", serviceKey)
                        .build())
                // 서버에 보낼 요청 바디가 JSON 형식
                .contentType(MediaType.APPLICATION_JSON)
                // 서버로부터 JSON 형식의 응답을 원함
                .accept(MediaType.APPLICATION_JSON)
                // 국세청 api에 사용할 요청 바디 폼
                .bodyValue(Map.of("b_no", List.of(corpNumber)))
                // WebClient 요청을 실제로 전송, 예외도 여기서 터짐
                .retrieve()
                // 서버로부터 받은 응답 바디를 XXXResDto 타입으로 역직렬화
                .bodyToMono(NtsCorpInfoResDto.CorpInfo.class)
                // 동기 -> 비동기 : 느리지만 스프링 MVC(DTO 리턴)는 비동기를 쓴다 (WebFlux(Mono 리턴)가 동기식 사용)
                // 응답이 올 때까지 현재 스레드 block (그래서 느림 비동기가)
                .block(Duration.ofSeconds(1));
    }
}