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
import com.project.dasihaebom.domain.resume.service.command.ResumeCommandService;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IntroductionCommandServiceImpl implements IntroductionCommandService {

    //Reoisitory
    private final IntroductionRepository introductionRepository;
    private final AnswerRepository answerRepository;
    private final WorkerRepository workerRepository;
    private final QuestionRepository questionRepository;

    //Service
    private final IntroductionQueryService introductionQueryService;
    private final ResumeCommandService resumeCommandService;
    private final GptService gptService;
    private final IntroductionPromptBuilder promptBuilder;

    @Override
    public Answer upsertAnswer(Long workerId, Long questionId, AnswerReqDto.SaveAnswerReqDto request) {

        // 1. workerId와 questionId로 기존 답변이 있는지 조회합니다.
        Optional<Answer> answerOpt = answerRepository.findByWorkerIdAndQuestionId(workerId, questionId);

        if (answerOpt.isPresent()) {
            // 2-1. [수정 로직] 이미 답변이 존재하면, 내용을 업데이트하고 반환합니다.
            Answer existingAnswer = answerOpt.get();
            existingAnswer.updateContent(request.content());
            return existingAnswer;

        } else {
            // 2-2. [생성 로직] 답변이 없으면, 새로 생성하여 저장하고 반환합니다.
            Worker worker = workerRepository.findById(workerId)
                    .orElseThrow(() -> new WorkerException(WorkerErrorCode.WORKER_NOT_FOUND));
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.QUESTION_NOT_FOUND));

            Answer newAnswer = IntroductionConverter.toAnswer(request, worker, question);
            return answerRepository.save(newAnswer);
        }
    }

    @Override
    public Introduction generateIntroduction(Long workerId) {
        // 1. 사용자 답변 전체 조회
        List<Answer> myAnswers = introductionQueryService.getMyAnswers(workerId);
        if (myAnswers.size() < 6) { // 답변을 모두 작성했는지 확인
            throw new IntroductionException(IntroductionErrorCode.ANSWERS_NOT_ENOUGH);
        }


        /* ==================gpt호출 임시 주석처리======================
        // 2. 자기소개서 본문 생성
        String fullTextPrompt = promptBuilder.buildFullTextPrompt(myAnswers);
        String generatedFullText = gptService.generate(fullTextPrompt);

        // 3. 한 줄 요약 생성
        String summaryPrompt = promptBuilder.buildSummaryPrompt(generatedFullText);
        String generatedSummary = gptService.generate(summaryPrompt);

         */
        String generatedFullText = "이것은 GPT를 호출하지 않고 생성된 자기소개서 본문 예시입니다. api 통신을 위해 임시 생성된 데이터입니다.";
        String generatedSummary = "성실함을 갖춘 테스트 인재";

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

        introductionRepository.save(newIntroduction);

        resumeCommandService.syncResume(workerId); //이력서 업데이트

        return newIntroduction;
    }



}
