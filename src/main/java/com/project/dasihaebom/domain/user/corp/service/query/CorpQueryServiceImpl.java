package com.project.dasihaebom.domain.user.corp.service.query;

import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CorpQueryServiceImpl implements CorpQueryService {

    private final CorpRepository corpRepository;

    @Override
    public Optional<Corp> findCorpByLoginId(String loginId) {
        return corpRepository.findByLoginId(loginId);
    }
}
