package com.project.dasihaebom.global.config;

import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.introduction.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        // DB에 질문 데이터가 없으면 초기 데이터 삽입
        if (questionRepository.count() == 0) {
            List<Question> questions = Arrays.asList(
                    Question.builder().question("Q1. 연령대별 일대기를 알려주세요.").build(),
                    Question.builder().question("Q2. 지원하는 직무나 분야는 무엇인가요?").build(),
                    Question.builder().question("Q3. 주요 경력이나 업무 경험 중 자랑하고 싶은 점이 있나요?").build(),
                    Question.builder().question("Q4. 학력 및 전공을 간단히 알려주세요.").build(),
                    Question.builder().question("Q5. 협업 경험 중 가장 기억에 남는 사례는 무엇인가요?").build(),
                    Question.builder().question("Q6. 자신의 강점과 약점은 무엇인가요?").build()
            );
            questionRepository.saveAll(questions);
        }
    }
}
