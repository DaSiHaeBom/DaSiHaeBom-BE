package com.project.dasihaebom.global.client.phonenumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneNumberClient {

    private final WebClient phoneNumberWebClient;

    @Value("${spring.message.endpoint}") String endPoint;
    @Value("${spring.message.callback}") String callback;
    @Value("${spring.message.api-key}") String apiKey;

    public void sendSms(String msg, String dstaddr) {
        // application/x-www-form-urlencoded 라는 전송 형식에 맞는 키-값 폼 컨테이너 생성
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        // 발급 받은 API 키를 폼에 추가
        form.add("api_key", apiKey);
        // 문자 내용을 폼에 추가 90 Bytes 해야지 싸다
        form.add("msg", msg);
        // 발신번호를 폼에 추가
        form.add("callback", callback);
        // 수신번호를 폼에 추가
        form.add("dstaddr", dstaddr);
        // 0 즉시 발송, 1 예약 발송
        form.add("send_reserve", "0");

        // POST 시작
        phoneNumberWebClient.post()
                // 주입받은 WebClient의 baseURL 뒤 엔드포인트 경로
                .uri(endPoint)
                // 폼 데이터를 요청 본문으로 설정
                .body(BodyInserters.fromFormData(form))
                // WebClient 요청을 실제로 전송, 예외도 여기서 터짐
                .retrieve()
                // 응답 본문을 문자열 Mono로 디코딩
                .bodyToMono(String.class)
                // 응답이 올 때까지 현재 스레드 block
                .block(Duration.ofSeconds(1));
    }
}
