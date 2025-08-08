package com.project.dasihaebom.domain.introduction.service.command;

import com.project.dasihaebom.domain.introduction.dto.request.AnswerReqDto;
import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Question;
import com.project.dasihaebom.domain.introduction.exception.IntroductionErrorCode;
import com.project.dasihaebom.domain.introduction.exception.IntroductionException;
import com.project.dasihaebom.domain.introduction.repository.AnswerRepository;
import com.project.dasihaebom.domain.introduction.repository.QuestionRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.exception.WorkerErrorCode;
import com.project.dasihaebom.domain.user.worker.exception.WorkerException;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IntroductionCommandServiceImpl implements IntroductionCommandService {
    private final AnswerRepository answerRepository;
    private final WorkerRepository workerRepository;
    private final QuestionRepository questionRepository;

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

        // 3. 새로운 Answer 엔티티를 생성합니다.
        var newAnswer = Answer.builder()
                .content(request.content())
                .worker(worker)
                .question(question)
                .build();

        // 4. 생성한 엔티티를 저장하고 반환합니다.
        return answerRepository.save(newAnswer);
    }

}
