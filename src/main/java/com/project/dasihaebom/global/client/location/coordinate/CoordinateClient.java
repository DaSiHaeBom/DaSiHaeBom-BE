package com.project.dasihaebom.global.client.location.coordinate;

import com.project.dasihaebom.global.client.location.coordinate.dto.KakaoCoordinateInfoResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinateClient {

    private final WebClient coordinateWebClient;

    @Value("${spring.kakao.local-api.map.coordinate.endpoint}") String endPoint;
    @Value("${spring.kakao.api-key}") String apiKey;

    public KakaoCoordinateInfoResDto getKakaoCoordinateInfo(String address){
        return coordinateWebClient.get()
                // EncodingMode.NONE 이므로 인코딩 하지는 않음
                .uri(uriBuilder -> uriBuilder
                        .path(endPoint)
                        .queryParam("query", address)
                        .build())
                // 카카오가 api key를 헤더로 넣으라고 함
                .header("Authorization", "KakaoAK " + apiKey)
                // WebClient 요청을 실제로 전송, 예외도 여기서 터짐
                .retrieve()
                // 서버로부터 받은 응답 바디를 XXXResDto 타입으로 역직렬화
                .bodyToMono(KakaoCoordinateInfoResDto.class)
                .block(Duration.ofSeconds(1));

    }
}
