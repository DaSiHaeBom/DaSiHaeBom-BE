package com.project.dasihaebom.domain.user.corp.service.query;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;

import java.util.Optional;

public interface CorpQueryService {

    CorpResDto.CorpProfileResDto getCorpProfile(long corpId, Role role);

    CorpResDto.CorpLoginIdResDto getCorpLoginId(CorpReqDto.CorpLoginIdReqDto corpLoginIdReqDto);

    CorpResDto.CorpCheckLoginIdResDto checkCorpLoginId(String loginId);
}
