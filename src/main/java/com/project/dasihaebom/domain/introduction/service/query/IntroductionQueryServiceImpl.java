package com.project.dasihaebom.domain.introduction.service.query;

import com.project.dasihaebom.domain.introduction.entity.Answer;
import com.project.dasihaebom.domain.introduction.entity.Introduction;
import com.project.dasihaebom.domain.introduction.exception.IntroductionErrorCode;
import com.project.dasihaebom.domain.introduction.exception.IntroductionException;
import com.project.dasihaebom.domain.introduction.repository.AnswerRepository;
import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
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
public class IntroductionQueryServiceImpl implements IntroductionQueryService {

    private final AnswerRepository answerRepository;
    private final IntroductionRepository introductionRepository;

    @Override
    public List<Answer> getMyAnswers(Long workerId) {
        // 해당 사용자의 모든 답변을 조회
        return answerRepository.findAllByWorkerId(workerId);
    }

    @Override
    public Answer getMyAnswer(Long workerId, Long questionId) {
        // 특정 사용자가 특정 질문에 대해 작성한 답변을 조회
        return answerRepository.findByWorkerIdAndQuestionId(workerId, questionId)
                .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.ANSWER_NOT_FOUND));
    }

    @Override
    public Introduction getMyIntroduction(Long workerId) {
        return introductionRepository.findTopByWorkerIdOrderByIdDesc(workerId)
                .orElseThrow(() -> new IntroductionException(IntroductionErrorCode.INTRODUCTION_NOT_FOUND));
    }


}
