package com.project.dasihaebom.domain.user.corp.converter;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import com.project.dasihaebom.global.client.corpNumber.dto.NtsCorpInfoResDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorpConverter {

    public static Corp toCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto) {
        return Corp.builder()
                .loginId(corpCreateReqDto.loginId())
                .ceoName(corpCreateReqDto.ceoName())
                .phoneNumber(corpCreateReqDto.phoneNumber())
                .corpNumber(corpCreateReqDto.corpNumber())
                .corpName(corpCreateReqDto.corpName())
                .corpAddress(corpCreateReqDto.corpAddress())
                .role(Role.CORP)
                .build();
    }

    public static CorpResDto.CorpNumberValidResDto toCorpNumberValidResDto(CorpReqDto.CorpNumberValidReqDto corpNumberValidReqDto, boolean isValid) {
        return CorpResDto.CorpNumberValidResDto.builder()
                .corpNumber(corpNumberValidReqDto.corpNumber())
                .isValid(isValid)
//                .corpInfo(corpInfo)
                .build();
    }
}
