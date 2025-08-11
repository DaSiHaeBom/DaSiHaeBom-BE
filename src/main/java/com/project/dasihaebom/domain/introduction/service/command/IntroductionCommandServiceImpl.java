package com.project.dasihaebom.domain.introduction.service.command;

import com.project.dasihaebom.domain.introduction.converter.IntroductionConverter;
import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.introduction.exception.IntroductionErrorCode;
import com.project.dasihaebom.domain.introduction.exception.IntroductionException;
import com.project.dasihaebom.domain.introduction.repository.AnswerRepository;
import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
import com.project.dasihaebom.domain.introduction.repository.QuestionRepository;
import com.project.dasihaebom.domain.introduction.service.query.IntroductionQueryService;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IntroductionCommandServiceImpl implements IntroductionCommandService {
    private final IntroductionRepository introductionRepository;
    private final AnswerRepository answerRepository;
    private final WorkerRepository workerRepository;
    private final QuestionRepository questionRepository;
    private final IntroductionQueryService introductionQueryService;
    private final GptService gptService;
    private final IntroductionPromptBuilder promptBuilder;

    @Override
    public Answer createAnswer(Long workerId, Long questionId, AnswerReqDto.CreateAnswerReqDto request) {
        // 1. 답변을 생성하기 전, 이미 작성한 답변이 있는지 확인
        if (answerRepository.existsByWorkerIdAndQuestionId(workerId, questionId)) {
            throw new IntroductionException(IntroductionErrorCode.ANSWER_ALREADY_EXISTS);
        }

        // 2. 답변과 연관된 Worker와 Question 엔티티를 DB에서 조회
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.QUESTION_NOT_FOUND));

        // 3. 새로운 Answer 엔티티를 생성
        Answer newAnswer = IntroductionConverter.toAnswer(request, worker, question);

        // 4. 생성한 엔티티를 저장하고 반환합니다.
        return answerRepository.save(newAnswer);
    }

    @Override
    public Answer updateAnswer(Long workerId, Long questionId, AnswerReqDto.UpdateAnswerReqDto request) {
        // 1. 수정할 Answer 엔티티를 DB에서 조회 없으면 예외를 발생
        Answer answer = answerRepository.findByWorkerIdAndQuestionId(workerId, questionId)
                .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.ANSWER_NOT_FOUND));

        // 2. 엔티티의 내용을 업데이트
        answer.updateContent(request.content());

        // 3. @Transactional에 의해 dirty checking이 일어나므로, save 호출 없이 자동으로 DB에 반영
        return answer;
    }

    @Override
    public Introduction generateIntroduction(Long workerId) {
        // 1. 사용자 답변 전체 조회
        List<Answer> myAnswers = introductionQueryService.getMyAnswers(workerId);
        if (myAnswers.size() < 6) { // 답변을 모두 작성했는지 확인
            throw new IntroductionException(IntroductionErrorCode.ANSWERS_NOT_ENOUGH);
        }

        // 2. 자기소개서 본문 생성
        String fullTextPrompt = promptBuilder.buildFullTextPrompt(myAnswers);
        String generatedFullText = gptService.generate(fullTextPrompt);

        // 3. 한 줄 요약 생성
        String summaryPrompt = promptBuilder.buildSummaryPrompt(generatedFullText);
        String generatedSummary = gptService.generate(summaryPrompt);

        // 4. 이전 자기소개서 삭제
        introductionRepository.deleteAllByWorkerId(workerId);

        // 5. 결과 저장
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));

        Introduction newIntroduction = Introduction.builder()
                .fullText(generatedFullText)
                .summary(generatedSummary)
                .worker(worker)
                .build();

        return introductionRepository.save(newIntroduction);
    }



}
