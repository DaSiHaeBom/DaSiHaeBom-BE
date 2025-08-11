package com.project.dasihaebom.domain.introduction.service.command;

import com.project.dasihaebom.domain.introduction.entity.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IntroductionPromptBuilder {

    //자소서 본문 프롬프트
    public String buildFullTextPrompt(List<Answer> answers) {
        String qaString = answers.stream()
                .map(answer -> String.format(
                        "Q: %s\nA: %s",
                        answer.getQuestion().getQuestion(),
                        answer.getContent()
                ))
                .collect(Collectors.joining("\n\n"));

        return qaString + "\n\n---\n\n위 Q&A 내용을 바탕으로, 구직을 위한 전문적인 자기소개서를 1~2 단락으로 작성해줘.";
    }

    //한 줄요약 프롬프트
    public String buildSummaryPrompt(String fullText) {
        return fullText + "\n\n---\n\n위 자기소개서 내용을 바탕으로, 이력서 목록에서 다른 사람의 시선을 끌 수 있는 매력적인 한 줄 요약(50자 이내)을 만들어 줘";
    }
}
