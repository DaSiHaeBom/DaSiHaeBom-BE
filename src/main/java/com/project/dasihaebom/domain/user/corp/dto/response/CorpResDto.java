package com.project.dasihaebom.domain.user.corp.dto.response;

import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.global.client.corpNumber.dto.NtsCorpInfoResDto;
import lombok.Builder;

import java.util.List;

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
            String corpAddress
    ) {
    }
}
