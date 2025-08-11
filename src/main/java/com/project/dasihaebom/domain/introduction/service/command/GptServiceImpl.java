package com.project.dasihaebom.domain.introduction.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class GptServiceImpl implements GptService {

    private final WebClient webClient;
    private final String model;

    // WebClient.Builder를 주입받아 초기 설정과 함께 WebClient 인스턴스를 생성
    public GptServiceImpl(WebClient.Builder webClientBuilder,
                          @Value("${gpt.model}") String model,
                          @Value("${gpt.api.key}") String apiKey,
                          @Value("${gpt.api.url}") String apiUrl) { //모델 url api키 주입
        this.model = model;
        this.webClient = webClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }


    // GPT API를 호출하여 프롬프트에 대한 응답을 생성
    @Override
    public String generate(String prompt) {
        GptRequest requestBody = new GptRequest(
                this.model,
                List.of(new GptRequest.Message("user", prompt))
        );

        try {
            GptResponse gptResponse = this.webClient.post() //post 요청 던지기
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GptResponse.class)
                    .block();

            if (gptResponse != null && !gptResponse.choices().isEmpty()) {
                return gptResponse.choices().get(0).message().content();
            }

        } catch (Exception e) {
            log.error("GPT API 호출 중 오류 발생", e);
            throw new RuntimeException("GPT API 호출에 실패했습니다.");
        }

        return "AI 응답을 생성하는 데 실패했습니다.";
    }

    // GPT API 요청/응답 DTO
    private record GptRequest(String model, List<Message> messages) {
        private record Message(
                String role,
                String content
        ) {}
    }

    private record GptResponse(List<Choice> choices) {
        private record Choice(Message message) {}
        private record Message(String content) {}
    }
}
