package com.project.dasihaebom.domain.user.corp.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import com.project.dasihaebom.domain.user.worker.converter.WorkerConverter;
import com.project.dasihaebom.domain.user.worker.dto.request.WorkerReqDto;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import com.project.dasihaebom.domain.user.worker.repository.WorkerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CorpCommandServiceImpl implements CorpCommandService {

    // Repo
    private final CorpRepository corpRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    // Service
    private final AuthCommandService authCommandService;


    @Override
    public void createCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto) {
        Corp corp = CorpConverter.toCorp(corpCreateReqDto);
        corpRepository.save(corp);

        authCommandService.savePassword(corp, encodePassword(corpCreateReqDto.password()));
    }

    // TODO : 일단 미구현
    private String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
        return rawPassword;
    }
}
