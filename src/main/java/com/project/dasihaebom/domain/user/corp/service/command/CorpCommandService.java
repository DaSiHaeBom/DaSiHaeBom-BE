package com.project.dasihaebom.domain.user.corp.service.command;

import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;

public interface CorpCommandService {

    void createCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto);

    void updateCorp(CorpReqDto.CorpUpdateReqDto corpUpdateReqDto, long corpId);

    CorpResDto.CorpNumberValidResDto validCorpNumber(CorpReqDto.CorpNumberValidReqDto corpNumberValidReqDto);
}
