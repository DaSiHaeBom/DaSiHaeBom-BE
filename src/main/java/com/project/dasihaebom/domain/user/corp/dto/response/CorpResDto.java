package com.project.dasihaebom.domain.user.corp.dto.response;

import com.project.dasihaebom.domain.user.Role;
import lombok.Builder;

public class CorpResDto {

    @Builder
    public record CorpNumberValidResDto(
            String corpNumber,
            boolean isValid
//            NtsCorpInfoResDto.CorpInfo corpInfo
    ) {
    }

    @Builder
    public record CorpProfileResDto(
            Role role,
            String ceoName,
            String phoneNumber,
            String corpNumber,
            String corpName,
            String corpBaseAddress,
            String corpDetailAddress
    ) {
    }

    @Builder
    public record CorpLoginIdResDto(
            String loginId
    ) {
    }

    @Builder
    public record CorpCheckLoginIdResDto(
            String loginId,
            boolean isAlreadyRegistered
    ) {
    }
}
