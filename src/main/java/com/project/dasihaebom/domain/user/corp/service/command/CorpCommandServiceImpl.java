package com.project.dasihaebom.domain.user.corp.service.command;

import com.project.dasihaebom.domain.auth.service.command.AuthCommandService;
import com.project.dasihaebom.domain.user.corp.converter.CorpConverter;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.domain.user.corp.exception.CorpErrorCode;
import com.project.dasihaebom.domain.user.corp.exception.CorpException;
import com.project.dasihaebom.domain.user.corp.repository.CorpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.project.dasihaebom.global.util.UpdateUtils.updateIfChanged;

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

    @Override
    public void updateCorp(CorpReqDto.CorpUpdateReqDto corpUpdateReqDto) {
        Corp corp = corpRepository.findById(1L)
                .orElseThrow(() -> new CorpException(CorpErrorCode.CORP_NOT_FOUND));

        updateIfChanged(corpUpdateReqDto.ceoName(), corp.getCorpName(), corp::changeCeoName);
        updateIfChanged(corpUpdateReqDto.phoneNumber(), corp.getPhoneNumber(), corp::changePhoneNumber);
        updateIfChanged(corpUpdateReqDto.corpNumber(), corp.getCorpNumber(), corp::changeCorpNumber);
        updateIfChanged(corpUpdateReqDto.corpName(), corp.getCorpName(), corp::changeCorpName);
        updateIfChanged(corpUpdateReqDto.corpAddress(), corp.getCorpAddress(), corp::changeCorpAddress);
    }

    // TODO : 일단 미구현
    private String encodePassword(String rawPassword) {
//        return passwordEncoder.encode(rawPassword);
        return rawPassword;
    }
}
