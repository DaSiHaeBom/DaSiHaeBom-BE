package com.project.dasihaebom.domain.user.corp.service.query;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
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
    public CorpResDto.CorpProfileResDto getCorpProfile(long corpId, Role role) {
        if (role.equals(Role.WORKER)) {
            throw new CorpException(CorpErrorCode.ROLE_IS_NOT_CORP);
        }

        Corp corp = corpRepository.findById(corpId)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        return CorpConverter.toCorpProfileResDto(corp);
    }
}
