package com.project.dasihaebom.domain.resume.service.query;

import com.project.dasihaebom.domain.introduction.repository.IntroductionRepository;
import com.project.dasihaebom.domain.resume.converter.ResumeConverter;
import com.project.dasihaebom.domain.resume.dto.request.ResumeSearchCondition;
import com.project.dasihaebom.domain.resume.dto.response.ResumeResDto;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.exception.ResumeErrorCode;
import com.project.dasihaebom.domain.resume.exception.ResumeException;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ResumeQueryServiceImpl implements ResumeQueryService {

    private final ResumeRepository resumeRepository;
    private final CorpRepository corpRepository;
    private final IntroductionRepository introductionRepository;

    @Override
    public ResumeResDto.ResumeCursorResponse searchResumes(ResumeSearchCondition condition, Long userCorpId) {

        // 검색을 요청한 기업 정보를 조회
        Corp userCorp = corpRepository.findById(userCorpId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        // 1. 레포지토리 호출 (요청된 size보다 +1개 더 조회)
        List<Tuple> resultTuples = resumeRepository.search(condition, userCorp);

        // 2. 다음 페이지 존재 여부 확인 및 리스트 슬라이싱
        boolean hasNext = resultTuples.size() > condition.getSize();
        if (hasNext) {
            resultTuples.remove(condition.getSize()); // 다음 페이지 확인용으로 가져온 마지막 데이터 제거
        }

        // 3. 조회된 데이터를 DTO 리스트로 변환
        List<ResumeResDto.ResumeSummaryDTO> content = resultTuples.stream()
                .map(tuple -> ResumeConverter.toResumeSummaryDTO(
                        tuple.get(0, Resume.class),
                        tuple.get(1, Double.class)
                ))
                .collect(Collectors.toList());

        // 4. 다음 페이지를 위한 커서 값 계산
        Long nextCursorId = null;
        Double nextCursorDistance = null;

        // 다음 페이지가 있을 경우에만 마지막 아이템을 기준으로 커서 값을 설정
        if (hasNext) {
            ResumeResDto.ResumeSummaryDTO lastItem = content.get(content.size() - 1);
            nextCursorId = lastItem.resumeId();

            // 거리순 정렬일 경우, 다음 커서에 거리 값도 포함
            if ("distance".equalsIgnoreCase(condition.getSortBy())) {
                nextCursorDistance = lastItem.distance();
            }
        }

        // 5. 최종적으로 Converter를 사용하여 API 응답 DTO 생성
        return ResumeConverter.toResumeCursorResponse(content, hasNext, nextCursorId, nextCursorDistance);
    }

    @Override
    public Resume getMyResume(Long workerId) {
        return resumeRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new ResumeException(ResumeErrorCode.RESUME_NOT_FOUND));
    }

    @Override
    public Resume getResumeByWorkerId(Long workerId) {
        return resumeRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new ResumeException(ResumeErrorCode.RESUME_NOT_FOUND));
    }

    @Override
    public boolean checkResumeExistence(Long workerId) {
        return introductionRepository.existsByWorker_Id(workerId);
    }


}
